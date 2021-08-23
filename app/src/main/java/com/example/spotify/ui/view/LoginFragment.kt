package com.example.spotify.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.spotify.R
import com.example.spotify.databinding.FragmentLoginBinding
import com.example.spotify.ui.view.SplashScreenActivity.Companion.prefs
import com.example.spotify.ui.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setup()
    }

    private fun setup() {

        binding.btLog.setOnClickListener {
            validate()
        }
        binding.btCreate.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_loginFragment2_to_registrationFragment)
        }
    }

    private fun accessToDetail() {
        startActivity(Intent(context, MainActivity::class.java))
        prefs.saveUser(binding.etEmail.editText?.text.toString())
        captureNameAndPhotoWhenStartingAssignment()
    }

    private fun captureNameAndPhotoWhenStartingAssignment() {
        viewModel.saveNameAndPhotoWhenStartingAssignment()
    }

    private fun login(){
        if (binding.etEmail.editText?.text!!.isNotEmpty() && binding.etPassword.editText?.text!!.isNotEmpty()) {
            auth.signInWithEmailAndPassword(
                binding.etEmail.editText?.text.toString(),
                binding.etPassword.editText?.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    accessToDetail()
                } else {
                    Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Llena los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate() {
        val result = arrayOf(validateEmail(), validatePassword())
        if (false in result) {
            return
        } else {
            login()
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.etEmail.editText?.text.toString()
        return if (email.isEmpty()) {
            binding.etEmail.error = "Field can not be empty"
            false
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email addres"
            false
        } else {
            binding.etEmail.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.etPassword.editText?.text.toString()
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@$%&+=])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$"
        )
        return if (password.isEmpty()) {
            binding.etPassword.error = "Field can not be empty"
            false
        } else if (passwordRegex.matcher(password).matches()) {
            binding.etPassword.error = "Password is too weak"
            false
        } else if (password.length < 6) {
            binding.etPassword.error = "Very short password"
            false
        }else {
            binding.etPassword.error = null
            true
        }
    }

}