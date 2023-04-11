package com.pan.mvvm.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pan.mvvm.R
import com.pan.mvvm.adapter.PickUpImageAdapter
import com.pan.mvvm.databinding.ActivityCreatePostBinding
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class CreatePostActivity : AppCompatActivity() {
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private lateinit var binding: ActivityCreatePostBinding
    private var spinner: Spinner? = null
    private val categoryNames = mutableListOf<String>()
    private var cateId: String? = null
    private var cateName: String? = null

    private val pickedImages: ArrayList<Uri> by lazy { arrayListOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        spinner = binding.spinner


        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.apply {
            val customView = LayoutInflater.from(this@CreatePostActivity).inflate(R.layout.custom_actionbar_title, null)
            val layoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )

            setCustomView(customView, layoutParams)
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowTitleEnabled(false)

            // Get a reference to the back button view in the custom view layout
            val backButton = customView.findViewById<Button>(R.id.back_button)

            // Set a click listener to the back button view
            backButton.setOnClickListener {
                // Handle back button click event
                finish()
            }
        }


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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
    }


    private fun checkPostDetail() {
        val cateName = binding.spinner.selectedItem.toString()
        val title = binding.editTitle.text.toString()
        val description = binding.editDesc.text.toString()
        val hasImage = (binding.postRecycler.adapter?.itemCount ?: 0) > 0
        var errorMsg: String? = null

        when {
            TextUtils.isEmpty(cateName) -> {
                errorMsg = "Choose One Category"
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
            cateId =createPathFromString(cateId.toString()),
            cateName = createPathFromString(cateName.toString()),
            title = createPathFromString(binding.editTitle.text.toString()),
            description = createPathFromString(binding.editDesc.text.toString()),
            files = getRealPathFromURI(pickedImages)
        )

    }


    private fun createPathFromString(value: String): RequestBody{
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
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
                    spinner?.adapter = adapter


                    spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            // Do something when an item is selected

                            val selectedItem = parent?.getItemAtPosition(position) as String

                            for (element in cateList) {
                                if (element.catename == selectedItem) {
                                    cateId = element.id.toString()
                                    cateName = element.catename
                                    break
                                }
                            }

                            Log.d("Spinner", "selected item id :$cateId")

                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

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

   /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }*/
}