package com.shoppitplus.fitlife.auth

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shoppitplus.fitlife.R
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.FragmentLoginBinding
import com.shoppitplus.fitlife.models.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val PREFS_NAME = "AppPrefs"
    private val TOKEN_KEY = "auth_token"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)



        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.getStarted.setOnClickListener {
            verifyInputs()
        }

        return binding.root
    }

    private fun verifyInputs() {
        val username = binding.regNumberEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()

        var isValid = true

        binding.regNumber.error = null
        binding.password.error = null

        if (username.isEmpty()) {
            binding.regNumber.error = "Username is required"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.password.error = "Password is required"
            isValid = false
        }

        if (!isValid) return

        doLogin(username, password)
    }

    private fun doLogin(username: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.getStarted.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).loginUser(
                    LoginRequest(
                        username,
                        password
                    )
                )

                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (!token.isNullOrEmpty()) {
                        saveUserToken(token)
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_login_to_homeScreen)
                    } else {
                        Toast.makeText(requireContext(), "Token missing in response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "Server error", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.getStarted.isEnabled = true
            }
        }
    }

    private fun saveUserToken(token: String) {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
        Log.d(ContentValues.TAG, "Token saved successfully: $token")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}