package com.adit.bangkit.plagroid.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityUserProfileBinding
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.utils.Constants

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var userDetails: User = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            //get user details dari intent sebagai parcelable extra
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etFirstName.isEnabled = false
        binding.etFirstName.setText(userDetails.firstName)
        binding.etLastName.isEnabled = false
        binding.etLastName.setText(userDetails.lastName)
        binding.etEmailReg.isEnabled = false
        binding.etEmailReg.setText(userDetails.email)

        binding.tvUserImage.setOnClickListener(this@UserProfileActivity)

        binding.btnSave.setOnClickListener {
            if (validateUserDetails()){
                showProgressDialog(resources.getString(R.string.please_wait))
                startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
            }
        }

    }

    private fun validateUserDetails():Boolean{
        return when{
            TextUtils.isEmpty(binding.etMobileNumber.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }

            TextUtils.isEmpty(binding.etAddress.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_address), true)
                false
            }

            else ->{
                showErrorSnackBar(resources.getString(R.string.please_wait), false)
                true
            }
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.tv_user_image ->{
                    if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )== PackageManager.PERMISSION_GRANTED
                    ){
                        showErrorSnackBar(resources.getString(R.string.storage_permission), false)
                    }else{
                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ),Constants.READ_STORAGE_PERMISSION_CODE
                        )
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
                showErrorSnackBar(resources.getString(R.string.read_storage_permission_granted), false)
            }else{
                Toast.makeText(this, resources.getString(R.string.read_storage_permission_denied),
                Toast.LENGTH_LONG).show()
            }
        }
    }

}