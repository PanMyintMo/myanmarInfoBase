package com.pan.mvvm.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pan.mvvm.adapter.PickUpImageAdapter
import com.pan.mvvm.databinding.ActivityCreatePostBinding
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class CreatePostActivity : AppCompatActivity() {
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private lateinit var binding: ActivityCreatePostBinding
    private var autoCompleteTextView: AutoCompleteTextView? = null
    private val categoryNames = mutableListOf<String>()
    private var cateId: String? = null
    private var cateName: String? = null

    private val pickedImages: ArrayList<Uri> by lazy { arrayListOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        autoCompleteTextView = binding.spinner


        //setup custom tool bar
        setSupportActionBar(binding.toolbarPost)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnPost.setOnClickListener {
            checkPostDetail()
        }

        binding.chooseProfileImage.setOnClickListener {
            chooseImages()
        }


        bindObserverResponseDetail()

        bindObserverCategoryName()
        //getAllCategoryName
        myanfobaseViewModel.getAllCategoryItem()

        setContentView(binding.root)
    }


    private fun chooseImages() {

        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        resultLauncher.launch(Intent.createChooser(intent, "Choose an images"))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                getPickedImages(result.data!!)
                updatePickedImages()
            }
        }


    private fun getPickedImages(data: Intent) {

        pickedImages.clear()

        if (data.clipData != null) {
            for (i in 0 until data.clipData!!.itemCount) {
                val uri = data.clipData!!.getItemAt(i).uri
                pickedImages.add(uri)
            }
        } else {
            val uri = data.data!!
            pickedImages.add(uri)
        }
    }

    private fun updatePickedImages() {
        val imageAdapter = PickUpImageAdapter(this, pickedImages)
        binding.postRecycler.adapter = imageAdapter
        binding.postRecycler.layoutManager =
            LinearLayoutManager(this@CreatePostActivity, LinearLayoutManager.VERTICAL, false)
    }


    private fun checkPostDetail() {
        val cateName = binding.spinner.text.toString()
        val title = binding.editTitle.text.toString()
        val description = binding.editDesc.text.toString()
        val hasImage = (binding.postRecycler.adapter?.itemCount ?: 0) > 0
        var errorMsg: String? = null

        when {
            cateName == "Choose Category" -> {
                errorMsg = "Please choose a category"
            }
            TextUtils.isEmpty(title) -> {
                errorMsg = "Title Require"
            }
            TextUtils.isEmpty(description) -> {
                errorMsg = "Description Require"
            }
            !hasImage -> {
                errorMsg = "At least one image is Required to upload!"
            }
        }

        if (errorMsg != null) {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        } else {
            requestCreateNewPost()
        }
    }

    private fun requestCreateNewPost() {
        myanfobaseViewModel.createNewPost(
            cateId = createPathFromString(cateId.toString()),
            cateName = createPathFromString(cateName.toString()),
            title = createPathFromString(binding.editTitle.text.toString()),
            description = createPathFromString(binding.editDesc.text.toString()),
            files = getRealPathFromURI(pickedImages)
        )
    }


    private fun createPathFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun getRealPathFromURI(uris: List<Uri?>): List<File> {
        val realPath = mutableListOf<File>()
        for (uri in uris) {
            val contentResolver = applicationContext.contentResolver
            val projection =
                arrayOf(MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE)
            val cursor =
                uri?.let { contentResolver.query(it, projection, null, null, null) }
            cursor?.let {
                if (it.moveToFirst()) {
                    val fileNameIndex =
                        it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                    val fileSizeIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
                    val fileName = it.getString(fileNameIndex)
                    val fileSize = it.getLong(fileSizeIndex)


                    if (fileSize <= 5 * 1024 * 1024) { // 5MB in bytes
                        val file = File(cacheDir, fileName)
                        val inputStream = contentResolver.openInputStream(uri)
                        val outputStream = FileOutputStream(file)
                        inputStream?.use { input ->
                            outputStream.use { output ->
                                input.copyTo(output)
                            }
                        }
                        if (file.exists() && file.length() == fileSize) {
                            realPath.add(file)
                        }
                    } else {
                        // handle the case where the file size is greater than 5MB
                        Toast.makeText(
                            this@CreatePostActivity,
                            "Fill size can not exceed 5 MB!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                cursor.close()
            }
        }
        return realPath
    }

    private fun bindObserverCategoryName() {
        myanfobaseViewModel.getAllCategoryLiveData.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val cateList = response.data
                    categoryNames.clear()
                    for (element in cateList!!) {
                        cateName = element.catename
                        categoryNames.add(cateName!!)
                    }
                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categoryNames
                    )
                    autoCompleteTextView?.setAdapter(adapter)


                    autoCompleteTextView?.setOnItemClickListener { parent, _, position, id ->
                        val selectedItem = parent.getItemAtPosition(position) as String
                        for (element in cateList) {
                            if (element.catename == selectedItem) {
                                cateId = element.id.toString()
                                cateName = element.catename
                                break
                            }
                        }
                        Log.d("AutoCompleteTextView", "selected item id :$cateId")
                    }
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}


                else -> {}
            }

        }
    }

    private fun bindObserverResponseDetail() {

        myanfobaseViewModel.createNewPostResponseDetail.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Toast.makeText(
                        this@CreatePostActivity,
                        "Post Create Successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("TAG", "${response.message}")
                }
                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(this@CreatePostActivity)
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
                else -> {}
            }

        }
    }
}