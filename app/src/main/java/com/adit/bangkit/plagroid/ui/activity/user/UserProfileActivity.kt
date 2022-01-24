package com.adit.bangkit.plagroid.ui.activity.user

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
import com.adit.bangkit.plagroid.databinding.ActivityUserProfileBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader
import java.io.IOException

open class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            //get user details dari intent sebagai parcelable extra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etFirstName.setText(mUserDetails.firstName)
        binding.etLastName.setText(mUserDetails.lastName)
        binding.etEmailReg.setText(mUserDetails.email)


        if (mUserDetails.profileComplete == 0){
            binding.tvTittle.text = resources.getString(R.string.tittle_complete_profile)
            binding.etFirstName.isEnabled = false
            binding.etLastName.isEnabled = false
            binding.etEmailReg.isEnabled = false
        }else{
            binding.tvTittle.text = resources.getString(R.string.title_edit_profile)
            setupActionBar()
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, binding.tvUserImage)
            if (mUserDetails.mobile != 0L){
                binding.etMobileNumber.setText(mUserDetails.mobile.toString())
            }
            binding.etAddress.setText(mUserDetails.address)
            binding.etPosCode.setText(mUserDetails.codepos)
            if (mUserDetails.gender == Constants.MALE){
                binding.btnGenderMale.isChecked
            }else{
                binding.btnGenderFemale.isChecked
            }
        }
        

        binding.tvUserImage.setOnClickListener(this@UserProfileActivity)

        binding.btnSave.setOnClickListener(this@UserProfileActivity)

    }

    private fun setupActionBar(){

        setSupportActionBar(binding.toolbarUserProfileActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        binding.toolbarUserProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }



    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.tv_user_image ->{
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

                R.id.btn_save -> {
                    if (validateUserProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null){
                           FireStoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        }else{
                           updateUserProfileDetails()

                        showErrorSnackBar(resources.getString(R.string.valid_profile), false)
                        }
                    }
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


                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, binding.tvUserImage)
                    }catch (e : IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
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



    private fun validateUserProfileDetails():Boolean{
        return when{
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



    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()

        val firstName = binding.etFirstName.text.toString().trim{ it <= ' '}
        if (firstName != mUserDetails.firstName){
            userHashMap[Constants.FIRST_NAME] = firstName
        }
        val lastName = binding.etLastName.text.toString().trim{ it <= ' '}
        if (lastName != mUserDetails.lastName){
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val email = binding.etEmailReg.text.toString().trim{ it <= ' '}
        if (email != mUserDetails.email){
            userHashMap[Constants.EMAIL] = email
        }

        val mobileNumber = binding.etMobileNumber.text.toString().trim { it <= ' ' }
        val address = binding.etAddress.text.toString().trim{ it <= ' '}
        val codepos = binding.etPosCode.text.toString().trim { it <= ' ' }
        val gender = if (binding.btnGenderFemale.isChecked){
            Constants.MALE
        }else{
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (address.isNotEmpty()){
            userHashMap[Constants.ADDRESS] = address
        }

        if (codepos.isNotEmpty()){
            userHashMap[Constants.CODEPOS] = codepos.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        FireStoreClass().updateUserProfileData(this, userHashMap)
    }



    fun userProfileUpdateSuccess(){
        hideProgresDialog()
        Toast.makeText(this@UserProfileActivity,
            resources.getString(R.string.profile_update_success), Toast.LENGTH_LONG).show()

        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }



    fun imageUploadSuccess(imageURL: String){
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

}