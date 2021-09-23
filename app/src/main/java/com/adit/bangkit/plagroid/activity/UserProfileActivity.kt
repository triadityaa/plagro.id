package com.adit.bangkit.plagroid.activity

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
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
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

        //mengunci edit text agar tidak dapat di edit dan menampilkan data user yang di isi saat registrasi
        binding.etFirstName.isEnabled = false
        binding.etFirstName.setText(mUserDetails.firstName)
        binding.etLastName.isEnabled = false
        binding.etLastName.setText(mUserDetails.lastName)
        binding.etEmailReg.isEnabled = false
        binding.etEmailReg.setText(mUserDetails.email)

        binding.tvUserImage.setOnClickListener(this@UserProfileActivity)

        binding.btnSave.setOnClickListener(this@UserProfileActivity)

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


                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, tv_user_image)
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
        val mobileNumber = et_mobile_number.text.toString().trim { it <= ' ' }
        val address = et_address.text.toString().trim{ it <= ' '}
        val codepos = et_pos_code.text.toString().trim { it <= ' ' }
        val gender = if (btn_gender_male.isChecked){
            Constants.MALE
        }else{
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (address.isNotEmpty()){
            userHashMap[Constants.ADDRESS] = address
        }

        if (codepos.isNotEmpty()){
            userHashMap[Constants.CODEPOS] = codepos.toLong()
        }

        userHashMap[Constants.GENDER] = gender

        FireStoreClass().updateUserProfileData(this, userHashMap)
    }



    fun userProfileUpdateSuccess(){
        hideProgresDialog()
        Toast.makeText(this@UserProfileActivity,
            resources.getString(R.string.profile_update_success), Toast.LENGTH_LONG).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }



    fun imageUploadSuccess(imageURL: String){
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

}