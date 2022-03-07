package com.adit.bangkit.plagroid.ui.activity.seller

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySellerProfileBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader
import java.io.IOException

class SellerProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySellerProfileBinding
    private lateinit var mSellerDetails: Seller
    private var mSelectedImageFileUri: Uri? = null
    private var mSellerProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        var mSellerDetails = Seller()
        supportActionBar?.hide()

        if (intent.hasExtra(Constants.EXTRA_SELLER_DETAILS)){
            //get seller details dari intent sebagai parcelable extra
            mSellerDetails = intent.getParcelableExtra(Constants.EXTRA_SELLER_DETAILS)!!
        }



            if (mSellerDetails.profileComplete == 0){
                binding.tvTittle.text = getString(R.string.complete_retail_profile)
            }else{
                binding.toolbarSellerProfileActivity.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
                binding.toolbarSellerProfileActivity.setOnClickListener {
                    val intent = Intent(this@SellerProfileActivity, SellerActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                binding.tvTittle.text = resources.getString(R.string.title_edit_profile)
                setupActionBar()
                GlideLoader(this@SellerProfileActivity).loadUserPicture(mSellerDetails.image, binding.tvSellerImage)
                binding.etRetailName.setText(mSellerDetails.retailName)
                if (mSellerDetails.mobile != 0L){
                    binding.etMobileNumber.setText(mSellerDetails.mobile.toString())
                }
                binding.etAddress.setText(mSellerDetails.address)
                if (mSellerDetails.codepos != 0){
                    binding.etPosCode.setText(mSellerDetails.codepos.toString())
                }
            }

        binding.tvSellerImage.setOnClickListener(this@SellerProfileActivity)

        binding.btnSaveSeller.setOnClickListener(this@SellerProfileActivity)
    }

    private fun setupActionBar(){

        setSupportActionBar(binding.toolbarSellerProfileActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        binding.toolbarSellerProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.tv_seller_image ->{
                    if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )== PackageManager.PERMISSION_GRANTED
                    ){
                        //showErrorSnackBar(resources.getString(R.string.storage_permission), false)
                        Constants.showImageChooser(this)
                    }else{
                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ),Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_save_seller -> {
                    validateSellerProfileDetails()
                    updateSellerProfileDetails()
                }
            }
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                Toast.makeText(this, resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG).show()
            }
        }
    }



    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        mSelectedImageFileUri = data.data!!


                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, binding.tvSellerImage)
                    }catch (e : IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this@SellerProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled", resources.getString(R.string.image_selection_failed))
        }
    }



    private fun validateSellerProfileDetails():Boolean{
        return when{
            TextUtils.isEmpty(binding.etRetailName.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_retail_name), true)
                false
            }

            TextUtils.isEmpty(binding.etMobileNumber.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }

            binding.etAddress.length() < 25 ->{
                showErrorSnackBar(resources.getString(R.string.address_length), true)
                false
            }

            TextUtils.isEmpty(binding.etAddress.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_address), true)
                false
            }

            TextUtils.isEmpty(binding.etPosCode.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_pos_code), true)
                false
            }
            else ->{
                showErrorSnackBar(resources.getString(R.string.please_wait), false)
                true
            }
        }
    }



    private fun updateSellerProfileDetails(){
        val sellerHashMap = HashMap<String, Any>()

        val retailName = binding.etRetailName.text.toString().trim{ it <= ' '}
        if (retailName != mSellerDetails.retailName){
            sellerHashMap[Constants.RETAIL_NAME] = retailName
        }

        val email = binding.etEmailSeller.text.toString().trim{ it <= ' '}
        if (email != mSellerDetails.email){
            sellerHashMap[Constants.EMAIL] = email
        }

        val mobileNumber = binding.etMobileNumber.text.toString().trim { it <= ' ' }
        val address = binding.etAddress.text.toString().trim{ it <= ' '}
        val codepos = binding.etPosCode.text.toString().trim { it <= ' ' }


        if (mSellerProfileImageURL.isNotEmpty()){
            sellerHashMap[Constants.IMAGE] = mSellerProfileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mSellerDetails.mobile.toString()){
            sellerHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (address.isNotEmpty()){
            sellerHashMap[Constants.ADDRESS] = address
        }

        if (codepos.isNotEmpty()){
            sellerHashMap[Constants.CODEPOS] = codepos.toLong()
        }



        sellerHashMap[Constants.COMPLETE_PROFILE] = 1

        FireStoreClass().updateSellerProfileData(this, sellerHashMap)
    }




    fun sellerProfileUpdateSuccess(){
        hideProgresDialog()
        Toast.makeText(this@SellerProfileActivity,
            resources.getString(R.string.profile_update_success), Toast.LENGTH_LONG).show()

    }

    fun loadSellerDetailsSuccess(seller: Seller){
        mSellerDetails = seller
        hideProgresDialog()

        GlideLoader(this@SellerProfileActivity).loadUserPicture(seller.image, binding.tvSellerImage)
        binding.etRetailName.setText(seller.retailName)
        binding.etEmailSeller.setText(seller.email)
        binding.etAddress.setText(seller.address)
        if (mSellerDetails.mobile != 0L){
            binding.etMobileNumber.setText(mSellerDetails.mobile.toString())
        }
        if (mSellerDetails.codepos != 0){
            binding.etPosCode.setText(mSellerDetails.codepos.toString())
        }
    }



    fun imageUploadSuccess(imageURL: String){
        mSellerProfileImageURL = imageURL
        updateSellerProfileDetails()
    }

}