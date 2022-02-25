package com.adit.bangkit.plagroid.ui.activity.user

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySettingsBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerProfileActivity
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
                    var mSellerDetails = Seller("", "", "", "", 0, 0, "", 0, 2)
                    if (mSellerDetails.profileComplete==0) {
                        val intent = Intent(this@SettingsActivity, SellerProfileActivity::class.java)
                        intent.putExtra(Constants.EXTRA_SELLER_DETAILS, mSellerDetails)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this@SettingsActivity, SellerActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                   }
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


    private fun validateRegisterDetails():Boolean{
        return when{
            TextUtils.isEmpty(binding.etFirstNameSetting.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etLastNameSetting.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmailSetting.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            else ->{
                showErrorSnackBar(resources.getString(R.string.please_wait), false)
                true
            }
        }
    }


    fun sellerRegistrationSuccess(){
        hideProgresDialog()

        Toast.makeText(
            this@SettingsActivity, resources.getString(R.string.msg_register_success)
            , Toast.LENGTH_LONG).show()
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