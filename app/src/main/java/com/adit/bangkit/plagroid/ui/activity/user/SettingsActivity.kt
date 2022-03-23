package com.adit.bangkit.plagroid.ui.activity.user

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySettingsBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.seller.LoginSellerActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerProfileActivity
import com.adit.bangkit.plagroid.ui.admin.AdminActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails: User
    private lateinit var profil: SharedPreferences
    private lateinit var seller: Seller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mUserDetails= User()
        seller = Seller()
        loginSession()

        disableEditProfile()

        binding.toolbarUserProfileActivity.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbarUserProfileActivity.setNavigationOnClickListener {
           onBackPressed()
        }

        binding.btnLogout.setOnClickListener(this)
        binding.tvEdit.setOnClickListener(this)
        binding.tvStore.setOnClickListener(this)
    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUsersDetails(this)
    }

    fun loadUserDetailsSuccess(user: User){
        mUserDetails = user
        hideProgresDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, binding.tvUserImageSetting)
        binding.etFirstNameSetting.setText(user.firstName)
        binding.etLastNameSetting.setText(user.lastName)
        binding.etEmailSetting.setText(user.email)
        if (mUserDetails.mobile != 0L){
            binding.etMobileNumberSetting.setText(mUserDetails.mobile.toString())
        }
        binding.etAddressSetting.setText(user.address)
        if (mUserDetails.codepos != 0){
            binding.etPosCodeSetting.setText(mUserDetails.codepos.toString())
        }
        if (user.gender == Constants.MALE){
            binding.btnGenderMale.isChecked
        }else{
            binding.btnGenderFemale.isChecked
        }
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun disableEditProfile(){
        binding.tvUserImageSetting.isEnabled = false
        binding.etFirstNameSetting.isEnabled = false
        binding.etLastNameSetting.isEnabled = false
        binding.etEmailSetting.isEnabled = false
        binding.etMobileNumberSetting.isEnabled = false
        binding.etAddressSetting.isEnabled = false
        binding.etPosCodeSetting.isEnabled = false
        binding.rgGender.isEnabled = false
    }


    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.tv_store -> {
                    FirebaseAuth.getInstance().signOut()
                    Intent(this@SettingsActivity, LoginSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun loginSession(){
        profil = getSharedPreferences(Constants.PLAGRO_PREFERENCES, MODE_PRIVATE)
        if (profil.getString(Constants.EXTRA_USER_DETAILS, null) != null) {
            when(mUserDetails.userType){
                0 -> {
                    val intent = Intent(this@SettingsActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@SettingsActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@SettingsActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


}