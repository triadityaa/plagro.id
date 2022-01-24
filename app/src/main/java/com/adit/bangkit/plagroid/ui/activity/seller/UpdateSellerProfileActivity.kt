package com.adit.bangkit.plagroid.ui.activity.seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adit.bangkit.plagroid.databinding.ActivityUpdateSellerProfileBinding
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader

class UpdateSellerProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUpdateSellerProfileBinding
    private lateinit var mSellerDetails: Seller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateSellerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun loadSellerDetailsSuccess(seller: Seller){
        mSellerDetails = seller
        hideProgresDialog()

//        GlideLoader(this@UpdateSellerProfileActivity).loadUserPicture(seller.image, binding.tvUserImageSetting)
//        binding.etFirstNameSetting.setText(user.firstName)
//        binding.etLastNameSetting.setText(user.lastName)
//        binding.etEmailSetting.setText(user.email)
//        if (mUserDetails.mobile != 0L){
//            binding.etMobileNumberSetting.setText(mUserDetails.mobile.toString())
//        }
//        binding.etAddressSetting.setText(user.address)
//        binding.etPosCodeSetting.setText(user.codepos)
//        if (user.gender == Constants.MALE){
//            binding.btnGenderMale.isChecked
//        }else{
//            binding.btnGenderFemale.isChecked
//        }
    }
}