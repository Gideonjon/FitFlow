package com.shoppitplus.fitlife

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.FragmentSignUpBinding
import com.shoppitplus.fitlife.models.RegisterRequest
import com.shoppitplus.fitlife.models.RegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val TAG = "SignUpFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.getStarted.setOnClickListener {
            verifyInputs()
        }

        return binding.root
    }

    private fun verifyInputs() {
        val fullName = binding.fullNameEt.text.toString().trim()
        val regNumber = binding.regNumberEt.text.toString().trim()
        val email = binding.emailEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        val userName = binding.userNameEt.text.toString().trim()

        var isValid = true

        // Clear old errors
        binding.fullName.error = null
        binding.regNumber.error = null
        binding.email.error = null
        binding.password.error = null
        binding.userName.error = null

        // ✅ Full Name validation
        if (fullName.isEmpty()) {
            binding.userName.error = "Full name is required"
            isValid = false
        } else if (!fullName.contains(" ")) {
            binding.userName.error = "Please enter your full name (first and last)"
            isValid = false
        }

        // ✅ Full Name validation
        if (userName.isEmpty()) {
            binding.userName.error = "User name is required"
            isValid = false
        } else if (!fullName.contains(" ")) {
            binding.userName.error = "Please enter your username"
            isValid = false
        }


        // ✅ Reg Number validation
        if (regNumber.isEmpty()) {
            binding.regNumber.error = "Registration number is required"
            isValid = false
        } else if (regNumber.length < 4) {
            binding.regNumber.error = "Registration number is too short"
            isValid = false
        }

        // ✅ Email validation
        if (email.isEmpty()) {
            binding.email.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Enter a valid email address"
            isValid = false
        }

        // ✅ Password validation
        if (password.isEmpty()) {
            binding.password.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.password.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (!isValid) {
            Log.d(TAG, "Validation failed.")
            return
        }

        // Proceed to create account
        registerUser(fullName, regNumber, email, password, userName)
    }

    private fun registerUser(
        fullName: String,
        regNumber: String,
        email: String,
        password: String,
        username: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        binding.getStarted.isEnabled = false


        val request = RegisterRequest(
            username = username,
            password = password,
            name = fullName,
            regNumber = regNumber,
            email = email
        )

        Log.d(TAG, "Sending registration request: $request")

        val call = RetrofitClient.instance(requireContext()).createAccount(request)
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                binding.getStarted.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!
                    Log.d(TAG, "Registration successful: ${result.message}, ID: ${result.userId}")
                    Toast.makeText(
                        requireContext(),
                        "Account created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_signUp_to_login)
                } else {
                    Log.e(TAG, "Registration failed: ${response.code()} ${response.message()}")
                    Toast.makeText(
                        requireContext(),
                        "Registration failed. Try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.getStarted.isEnabled = true
                Log.e(TAG, "Error during registration: ${t.message}")
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
