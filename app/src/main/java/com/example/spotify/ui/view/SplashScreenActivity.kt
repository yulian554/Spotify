package com.example.spotify.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spotify.databinding.ActivitySplashScreenBinding
import com.example.spotify.preference.UserInfoPreference

class SplashScreenActivity : AppCompatActivity() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var prefs: UserInfoPreference
    }
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = UserInfoPreference(applicationContext)
        splash()

    }

    private fun splash(){
        binding.ivSpotify.alpha = 0f
        binding.ivSpotify.animate().setDuration(2000).alpha(1f).withEndAction {
            if (prefs.getUser().isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

}