package com.adit.bangkit.plagroid.ui.activity.seller

import android.os.Bundle
import com.adit.bangkit.plagroid.databinding.ActivitySellerBinding
import com.adit.bangkit.plagroid.ui.activity.BaseActivity

class SellerActivity : BaseActivity() {
    private lateinit var binding: ActivitySellerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val sharedPreferences = getSharedPreferences(Constants.PLAGRO_PREFERENCES, Context.MODE_PRIVATE)
//        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,  "")!!
//        binding.tvMain.text =
//            resources.getString(R.string.hello_user) +
//                    username +
//                    resources.getString(R.string.welcome)
    }
}