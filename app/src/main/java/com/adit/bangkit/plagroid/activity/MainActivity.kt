package com.adit.bangkit.plagroid.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityMainBinding
import com.adit.bangkit.plagroid.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(Constants.PLAGRO_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,  "")!!
        val text = getString(R.string.hello_user, username)
        binding.tvMain.text = text
    }
}