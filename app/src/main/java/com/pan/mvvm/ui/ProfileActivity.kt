package com.pan.mvvm.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pan.mvvm.R
import com.pan.mvvm.databinding.ActivityProfileBinding
import com.pan.mvvm.models.ProfileResponse
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()
    private lateinit var binding: ActivityProfileBinding
    private var gender: String? = null
    private var id: String? = null
    private var uri: Uri? = null

    // TOKEN MANAGER MUST BE INITIALIZED BEFORE ON_CREATE
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.editProfile.setOnClickListener {
            binding.editProfileScrollView.isVisible = true
            binding.profileScrollView.isVisible = false


        }

        initDataPicker()
        // Check which radio button is selected

        gender = when (binding.genderGroup.checkedRadioButtonId) {
            R.id.maleButton -> "Male"
            R.id.femaleButton -> "Female"
            else -> ""
        }
        binding.genderText.text = "Selected gender: $gender"

        binding.floatingAction.setOnClickListener {
            pickUpImage()
        }

        binding.submit.setOnClickListener {
            checkProfileDetail()
        }

        observeUserProfileDetail()

        // observe existing profile details
        myanfobaseViewModel.getLoginDetailResponseLiveData.observe(this) { userLoginDetailResponse ->
            populateUserDetails(
                ProfileResponse(
                    address = userLoginDetailResponse.address,
                    bio = userLoginDetailResponse.bio,
                    dob = userLoginDetailResponse.dob,
                    email = userLoginDetailResponse.email,
                    gender = userLoginDetailResponse.gender,
                    id = userLoginDetailResponse.id,
                    isAdmin = userLoginDetailResponse.isAdmin,
                    login = userLoginDetailResponse.login,
                    profilePicture = userLoginDetailResponse.profilePicture,
                    username = userLoginDetailResponse.username,
                )
            )
        }

        // read existing profile details
        tokenManager.getId()
            ?.let { userId -> myanfobaseViewModel.getUserLoginDetailResponse(userId) }

    }

    private fun populateUserDetails(profileResponse: ProfileResponse) {
        val profileUrl = profileResponse.profilePicture[0].filePath
        Glide.with(this).load(profileUrl).into(binding.postImageProfile)

        binding.textUserName.text = profileResponse.username
        binding.textEmail.text = profileResponse.email
        binding.textAddress.text = profileResponse.address
        binding.textBio.text = profileResponse.bio
        binding.textGender.text = profileResponse.gender

    }

    private fun checkProfileDetail() {
        val username = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val address = binding.editAddress.text.toString()
        val dob = binding.dob.text.toString()
        val bio = binding.editBio.text.toString()
        val profileImage = binding.postImage.drawable != null

        var errorMsg: String? = null


        //for navHeader
        id = tokenManager.getId()

        when {
            TextUtils.isEmpty(username) -> {
                errorMsg = "Username is required"
            }
            TextUtils.isEmpty(email) -> {
                errorMsg = "Email is required"
            }
            TextUtils.isEmpty(address) -> {
                errorMsg = "Address is required"
            }
            TextUtils.isEmpty(bio) -> {
                errorMsg = "Bio is required"
            }
            TextUtils.isEmpty(dob) -> {
                errorMsg = "Choose date of birth"
            }
            !profileImage -> {
                errorMsg = "Profile image is required"
            }
        }

        if (errorMsg != null) {
            // show error message to the user
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        } else {

            updateProfile()
            binding.editProfileScrollView.isVisible = false
            binding.profileScrollView.isVisible = true
        }

    }

    private fun observeUserProfileDetail() {

        myanfobaseViewModel.updateUserProfileDetail.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {

                    response.data?.let { populateUserDetails(it) }

                    Toast.makeText(this, "Profile Detail is Successful!1", Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Error -> {
                    // Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    MaterialAlertDialogBuilder(this@ProfileActivity)
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
                is NetworkResult.Loading -> {

                }
                else -> {

                }
            }
        }
    }

    private fun pickUpImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        resultLauncher.launch(Intent.createChooser(intent, "Choose an images"))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                getPickedImages(result.data!!)
            }
        }

    private fun getPickedImages(data: Intent) {

        uri = data.data
        if (uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            binding.postImage.setImageBitmap(bitmap)
            true
        } else {
            false
        }

    }

    private fun updateProfile() {
        myanfobaseViewModel.updateUserProfileDetail(
            id.toString(),
            profilePicture = File(getRealPathFromURI(uri)),
            username = createPartFromString(binding.editName.text.toString()),
            email = createPartFromString(binding.editEmail.text.toString()),
            address = createPartFromString(binding.editAddress.text.toString()),
            dob = createPartFromString(binding.dob.text.toString()),
            gender = createPartFromString(gender.toString()),
            bio = createPartFromString(binding.editBio.text.toString())
        )
    }

    private fun createPartFromString(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }


    private fun getRealPathFromURI(uri: Uri?): String {
        var realPath = ""
        uri?.let {
            val contentResolver = applicationContext.contentResolver
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(it, projection, null, null, null)
            cursor?.let {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                realPath = cursor.getString(columnIndex)
                cursor.close()
            }
        }
        return realPath
    }


    private fun initDataPicker() {
        binding.datePickerButton.setOnClickListener {
            //Get Current Date
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)
            //Initialize DatePickerDialog

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                // Do something with the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val formattedDate = SimpleDateFormat("MMM dd yyyy").format(selectedDate.time)
                binding.dob.text = formattedDate
            }, year, month, day)

            // Show DatePickerDialog
            datePickerDialog.show()
        }
    }
}