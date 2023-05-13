package com.pan.mvvm.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.pan.mvvm.databinding.FragmentVerifyResetEmailBinding
import com.pan.mvvm.models.ActualResetRequestModel
import com.pan.mvvm.models.ResetRequestModel
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text
import javax.inject.Inject

@AndroidEntryPoint
class VerifyResetEmailFragment : Fragment() {
    private var _binding: FragmentVerifyResetEmailBinding? = null
    private val binding get() = _binding!!
    private var newPassword = ""

    @Inject
    lateinit var tokenManager: TokenManager
    private var userId: String? = null
    private lateinit var resetString: String


    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyResetEmailBinding.inflate(layoutInflater, container, false)


        userId = tokenManager.getId() ?: "userId"

        arguments.let {
            resetString = it?.getString("resetString", "") ?: ""
        }


        //bindObserverForActualResetPassword
        bindObserverActualResetPassword()

        //bindObserverForResetPassword
        bindObserverResetPassword()
        binding.resetBtn.setOnClickListener {
            authViewModel.resetPassword(resetRequest())
            /*binding.resetLayout.visibility = View.GONE
            binding.actualResetlayout.visibility = View.VISIBLE*/
        }

        binding.submit.setOnClickListener {
            checkingPassword()
        }


        return binding.root
    }

    private fun bindObserverActualResetPassword() {
        authViewModel.passwordResetLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    "Password reset is successful"
                }
                is NetworkResult.Error -> {
                    "Error"
                }
                is NetworkResult.Loading -> {
                    "Loading"
                }
                else -> {


                }
            }

        }
    }

    private fun checkingPassword() {
        val password = binding.password.text.toString()
        val confirmPass = binding.confirmPass.text.toString()


        when {
            TextUtils.isEmpty(password) -> {
                Toast.makeText(requireContext(), "Please enter a password", Toast.LENGTH_SHORT)
                    .show()
            }
            TextUtils.isEmpty(confirmPass) -> {
                Toast.makeText(requireContext(), "Please confirm your password", Toast.LENGTH_SHORT)
                    .show()
            }
            password != confirmPass -> {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                newPassword = password
                // Passwords match, call passwordReset()
               // authViewModel.passwordReset()

            }
        }
    }


    private fun bindObserverResetPassword() {
        authViewModel.resetPasswordLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val message = response.data?.message

                    if (message != null) {
                        binding.resetLayout.visibility = View.GONE
                        binding.actualResetlayout.visibility = View.VISIBLE


                        val tokenRegex = "reset_token=([^&]+)".toRegex()
                        val matchResult = tokenRegex.find(message)
                        val resetToken = matchResult?.groupValues?.get(1)
                        // Extract new password
                        //val newPassword = binding.password.text.toString()

                        // Make API call to reset password
                        authViewModel.passwordReset(
                            ActualResetRequestModel(
                                userId.toString(),
                                resetString,
                                newPassword
                            )
                        )

                    }

                    Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    val status = response.data?.status
                    Toast.makeText(requireContext(), "$status", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "Something wrong", Toast.LENGTH_SHORT).show()
                }

                else -> {

                }
            }

        }
    }

    private fun resetRequest(): ResetRequestModel {

        val redirectUrl = "https://www.myanfobase.com/passwordreset"
        val email = binding.resetPassText.text.toString()

        return ResetRequestModel(email, redirectUrl)
    }


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }


    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
