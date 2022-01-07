package com.adit.bangkit.plagroid.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySettingsBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class SettingsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.toolbarUserProfileActivity.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbarUserProfileActivity.setNavigationOnClickListener {
            val intent = Intent(this@SettingsActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        disableEditProfile()

        binding.btnLogout.setOnClickListener(this)
        binding.tvEdit.setOnClickListener(this)
    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUsersDetails(this)
    }

    fun loadUserDetailsSuccess(user: User){
        mUserDetails = user
        hideProgresDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, tv_user_image)
        binding.etFirstNameSetting.setText(user.firstName)
        binding.etLastNameSetting.setText(user.lastName)
        binding.etEmailSetting.setText(user.email)
        if (mUserDetails.mobile != 0L){
            et_mobile_number_setting.setText(mUserDetails.mobile.toString())
        }
        binding.etAddressSetting.setText(user.address)
        binding.etPosCodeSetting.setText(user.codepos)
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
                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

}