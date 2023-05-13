package com.pan.mvvm.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pan.mvvm.R
import com.pan.mvvm.databinding.FragmentLoginBinding
import com.pan.mvvm.models.LoginRequestModel
import com.pan.mvvm.ui.MainActivity
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.btnLogin.setOnClickListener {

            val validationResult = checkLoginDetail()

            if (validationResult.first) {
                authViewModel.loginUser(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }

        }

        binding.resetPassword.setOnClickListener {


            val data=activity?.intent?.data

            val resetString = data?.getQueryParameter("resetString") ?: ""


            /*  val uri = Uri.parse("https://www.myanfobase.com/api/users/requestPasswordReset/")
              val resetString = uri.getQueryParameters("resetString") ?: ""
  */
            val action =
                LoginFragmentDirections.actionLoginFragmentToVerifyResetEmail(resetString.toString())
            findNavController().navigate(action)

        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        bindObservers()

        return binding.root
    }

    private fun getUserRequest(): LoginRequestModel {
        val loginemail = binding.emailEt.text.toString()
        val loginpassword = binding.txtPassword.text.toString()

        return LoginRequestModel(loginemail, loginpassword)
    }

    private fun checkLoginDetail(): Pair<Boolean, String> {

        val loginRequest = getUserRequest()
        return authViewModel.validateLoginCredentials(
            loginRequest.loginemail,
            loginRequest.loginpassword
        )
    }

    private fun bindObservers() {
        authViewModel.loginResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false

            when (it) {
                is NetworkResult.Success -> {
                    //save token if login successful
                    tokenManager.saveToken(it.data!!.token)
                    tokenManager.saveId(it.data.id)

                    Toast.makeText(requireContext(), "Login success", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)

                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                else -> {}
            }
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setDrawerLocked(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).setDrawerLocked(false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}