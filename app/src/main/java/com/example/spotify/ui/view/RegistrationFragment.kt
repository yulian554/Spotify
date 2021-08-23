package com.example.spotify.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.spotify.R
import com.example.spotify.databinding.FragmentRegistrationBinding
import com.example.spotify.ui.view.SplashScreenActivity.Companion.prefs
import com.example.spotify.data.network.AuthenticationService
import com.example.spotify.data.network.FirestoreDbService
import com.example.spotify.ui.viewmodel.FavoritesViewModel
import com.example.spotify.ui.viewmodel.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    private val viewModel: RegistrationViewModel by viewModels()
    private val service = AuthenticationService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        setup()
    }

    private fun setup() {
        binding.btCreate.setOnClickListener { view ->
            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()
                && binding.etName.text.isNotEmpty() && binding.etSurname.text.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        service.saveDataUser(binding.etName.text.toString(), binding.etSurname.text.toString())
                        accessToDetail(view)
                        viewModel.getSong()
                    } else {
                        Toast.makeText(context, "Este usuario ya existe", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Llena los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun accessToDetail(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_registrationFragment_to_loginFragment2)
        Toast.makeText(context, "Usuario creado", Toast.LENGTH_SHORT).show()
        prefs.saveNameUser(binding.etName.text.toString())
    }
}
