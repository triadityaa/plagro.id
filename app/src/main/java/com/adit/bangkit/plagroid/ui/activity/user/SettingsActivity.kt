package com.adit.bangkit.plagroid.ui.activity.user

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySettingsBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerProfileActivity
import com.adit.bangkit.plagroid.ui.activity.user.fragments.DashboardFragment
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

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
            val intent = Intent(this@SettingsActivity, DashboardFragment::class.java)
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


    fun sellerLoggedInSuccess(seller: Seller){
        //hide progress bar
        hideProgresDialog()

        //userType 0 = admin...... userType 1 = user..... userType 2 = seller
        if (seller.userType == 2){
            if (seller.profileComplete == 0){
                //jika profile user belum complete arahkan user ke activity SellerProfileActivity
                val intent = Intent(this@SettingsActivity, SellerProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                startActivity(intent)
            }else{
                //jika profile seller sudah complete langsung arahkan ke SellerActivity
                val intent = Intent(this@SettingsActivity, SellerActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                startActivity(intent)
            }
        }else{
            hideProgresDialog()
            showErrorSnackBar(R.string.msg_login_seller_unsuccess.toString(), true)
            finish()
        }
    }

}