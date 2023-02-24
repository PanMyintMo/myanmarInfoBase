package com.pan.mvvm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pan.mvvm.R
import com.pan.mvvm.databinding.FragmentRegisterBinding
import com.pan.mvvm.models.RegisterRequestModel
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)


        if (tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
        binding.btnSignUp.setOnClickListener {

            val validationResult = checkDetailRegister()

            if (validationResult.first) {
                authViewModel.registerUser(getUserRequest())
            }

            else{
                binding.txtError.text=validationResult.second
            }
            //  findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }


        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        bindObservers()

        return binding.root
    }


    private fun getUserRequest(): RegisterRequestModel {
        val username = binding.txtUserName.text.toString()
        val email = binding.txtEmail.text.toString()
        val pass = binding.txtPassword.text.toString()
        return RegisterRequestModel(username, email, pass)
    }

    private fun checkDetailRegister(): Pair<Boolean, String> {

    //    val confirmPass = binding.txtConfirmPassword.text.toString()

        val userRequestModel = getUserRequest()

        return authViewModel.validateCredentials(
            userRequestModel.username,
            userRequestModel.email,
            userRequestModel.password
        )

/*        if (TextUtils.isEmpty(username)) {
            binding.txtUserName.error = "Name is required"
            binding.txtUserName.requestFocus()
        } else if (TextUtils.isEmpty(email)) {
            binding.txtEmail.error = "Email is required"
            binding.txtEmail.requestFocus()
        } else if (TextUtils.isEmpty(pass)) {
            binding.txtPassword.error = "Password is required"
            binding.txtPassword.requestFocus()
        } else if (TextUtils.isEmpty(confirmPass)) {
            binding.txtConfirmPassword.error = "Confirm Password is required"
            binding.txtConfirmPassword.requestFocus()
        } else {
            if (pass != confirmPass) {
                Toast.makeText(
                    requireContext(),
                    "Password are not matched",
                    Toast.LENGTH_SHORT
                ).show()
                binding.txtPassword.requestFocus()
                binding.txtConfirmPassword.requestFocus()
            } else {
                setupRegister(username, email, pass)
            }
        }*/
    }

    private fun bindObservers() {
        authViewModel.registerResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false

            when (it) {
                is NetworkResult.Success -> {
                    //token
                //    tokenManager.saveToken(it.data!!.status)

                    Toast.makeText(requireContext(), "Register success", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)

                }
                is NetworkResult.Error -> {

                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }
        })
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