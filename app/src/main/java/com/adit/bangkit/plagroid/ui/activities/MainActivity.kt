package com.adit.bangkit.plagroid.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.adit.bangkit.plagroid.databinding.ActivityMainBinding
import com.adit.bangkit.plagroid.utils.Constants

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create an instance of Android SharedPreferences
        val sharedPreferences =
            getSharedPreferences(Constants.PLAGRO_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        // Set the result to the tv_main.
        binding.tvMain.text= "The logged in user is $username."
    }
}