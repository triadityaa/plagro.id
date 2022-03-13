package com.adit.bangkit.plagroid.ui.activity.seller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySellerBinding
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.user.DashboardActivity
import com.adit.bangkit.plagroid.ui.admin.AdminActivity
import com.adit.bangkit.plagroid.utils.Constants

class SellerActivity : BaseActivity() {
    private lateinit var binding: ActivitySellerBinding
    private lateinit var mUserDetails: User
    private lateinit var profil: SharedPreferences
    private lateinit var seller: Seller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            //get user details dari intent sebagai parcelable extra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        mUserDetails= User()
        seller = Seller()
        loginSession()

        val sharedPreferences = getSharedPreferences(Constants.PLAGRO_PREFERENCES, Context.MODE_PRIVATE)
        val retailName = sharedPreferences.getString(mUserDetails.retailName,  "")!!
        binding.tvMain.text =
            resources.getString(R.string.hello_user) +
                    retailName +
                    resources.getString(R.string.welcome)
    }

    private fun loginSession(){
        profil = getSharedPreferences(Constants.PLAGRO_PREFERENCES, MODE_PRIVATE)
        if (profil.getString(Constants.EXTRA_USER_DETAILS, null) != null) {
            when(mUserDetails.userType){
                0 -> {
                    val intent = Intent(this@SellerActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@SellerActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@SellerActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

}