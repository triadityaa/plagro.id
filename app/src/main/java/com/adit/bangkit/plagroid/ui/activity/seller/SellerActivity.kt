package com.adit.bangkit.plagroid.ui.activity.seller

import android.content.Context
import android.os.Bundle
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySellerBinding
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.utils.Constants

class SellerActivity : BaseActivity() {
    private lateinit var binding: ActivitySellerBinding
    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            //get user details dari intent sebagai parcelable extra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        val sharedPreferences = getSharedPreferences(Constants.PLAGRO_PREFERENCES, Context.MODE_PRIVATE)
        val retailName = sharedPreferences.getString(mUserDetails.retailName,  "")!!
        binding.tvMain.text =
            resources.getString(R.string.hello_user) +
                    retailName +
                    resources.getString(R.string.welcome)
    }
}