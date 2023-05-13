package com.pan.mvvm.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pan.mvvm.R
import com.pan.mvvm.databinding.ActivityProfileBinding
import com.pan.mvvm.models.ProfileResponse
import com.pan.mvvm.utils.Constants.PROFILE_IMAGE_URI_KEY
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
        //setup custom tool bar
        setSupportActionBar(binding.toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        initDataPicker()
        // Check which radio button is selected

        binding.floatingAction.setOnClickListener {
            pickUpImage()
        }

        binding.backButton.setOnClickListener {
            MaterialAlertDialogBuilder(this@ProfileActivity)
                .setTitle("Discard?")
                .setMessage("Do you want to discard?")
                .setPositiveButton("Ok") { _, _ ->
                    finish()

                }
                .setNegativeButton("No") { _, _ ->
                    Snackbar.make(binding.root, "Declined", Snackbar.LENGTH_SHORT).show()
                }
                .show()
        }

        binding.editProfile.setOnClickListener {
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


    private fun checkProfileDetail() {
        val username = binding.editName.text.toString()
        val email = binding.editEmail.text.toString()
        val address = binding.editAddress.text.toString()
        val dob = binding.textdob.text.toString()
        val bio = binding.editBio.text.toString()
        val profileImage = binding.postImage.drawable != null

        gender = when (binding.genderGroup.checkedRadioButtonId) {
            R.id.maleButton -> "Male"
            R.id.femaleButton -> "Female"
            else -> ""
        }
        binding.genderText.text = "Selected gender: $gender"


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

            TextUtils.isEmpty(gender) -> {
                errorMsg = "Choose gender"
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
            val imagePath = getRealPathFromURI(uri)
            File(imagePath)
            binding.postImage.setImageURI(uri)
            // Save the image file path in SharedPreferences
            val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(PROFILE_IMAGE_URI_KEY, imagePath).apply()
            true
        } else {
            false
        }
    }

    private fun updateProfile() {
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val savedImagePath = sharedPreferences.getString(PROFILE_IMAGE_URI_KEY, null)
        val imageFile =
            savedImagePath?.let { File(it) } ?: uri?.let { File(getRealPathFromURI(uri)) }


        imageFile?.let {
            myanfobaseViewModel.updateUserProfileDetail(
                id.toString(),
                profilePicture = it,
                username = createPartFromString(binding.editName.text.toString()),
                email = createPartFromString(binding.editEmail.text.toString()),
                address = createPartFromString(binding.editAddress.text.toString()),
                dob = createPartFromString(binding.dob.text.toString()),
                gender = createPartFromString(gender.toString()),
                bio = createPartFromString(binding.editBio.text.toString())
            )
        }
    }

    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
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

            val datePickerDialog = DatePickerDialog(this, { _, years, month, dayOfMonth ->
                // Do something with the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(years, month, dayOfMonth)
                val formattedDate = SimpleDateFormat("MMM dd yyyy").format(selectedDate.time)
                binding.dob.text = formattedDate
            }, year, month, day)

            // Show DatePickerDialog
            datePickerDialog.show()
        }
    }

    private fun observeUserProfileDetail() {

        myanfobaseViewModel.updateUserProfileDetail.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { populateUserDetails(it) }
                    Toast.makeText(this, "Profile Detail is Successful!", Toast.LENGTH_SHORT)
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

    private fun populateUserDetails(profileResponse: ProfileResponse) {

        val profileUrl = profileResponse.profilePicture[0].filePath
        Glide.with(this).load(profileUrl).into(binding.postImageProfile)

        binding.textUserName.text = profileResponse.username
        binding.textEmail.text = profileResponse.email
        binding.textAddress.text = profileResponse.address
        binding.textBio.text = profileResponse.bio
        binding.textGender.text = profileResponse.gender
        binding.textdob.text = profileResponse.dob

        //For Edit Profile
        binding.editName.text?.clear()
        binding.editName.append(profileResponse.username)
        binding.editEmail.text?.clear()
        binding.editEmail.append(profileResponse.email)
        binding.editAddress.text?.clear()
        binding.editAddress.append(profileResponse.address)
        binding.editBio.text?.clear()
        binding.editBio.append(profileResponse.bio)
        binding.dob.text = ""
        binding.dob.append(profileResponse.dob)

        when (profileResponse.gender) {
            "Male" -> binding.maleButton.isChecked = true
            "Female" -> binding.femaleButton.isChecked = true
            else -> {
                binding.maleButton.isChecked = false
                binding.femaleButton.isChecked = false
            }
        }

        val editProfileUrl = profileResponse.profilePicture[0].filePath
        Glide.with(this).load(editProfileUrl).into(binding.postImage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.editProfile -> {
                binding.editProfileScrollView.isVisible = true
                binding.profileScrollView.isVisible = false

            }
        }
        return super.onOptionsItemSelected(item)
    }
}