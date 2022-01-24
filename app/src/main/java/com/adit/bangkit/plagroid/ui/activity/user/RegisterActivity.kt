package com.adit.bangkit.plagroid.ui.activity.user

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityRegisterBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerActivity
import com.adit.bangkit.plagroid.ui.admin.AdminActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: User
    private lateinit var seller: Seller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        binding.toolbarRegisterActivity.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbarRegisterActivity.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnRegister.setOnClickListener {
            validateRegisterDetails()
            registerUser()
        }


        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            when(user.userType){
                0 -> {
                    val intent = Intent(this@RegisterActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@RegisterActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateRegisterDetails():Boolean{
        return when{
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmailReg.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPasswordReg.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            binding.etPasswordReg.length() < 8 ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password_length), true)
                false
            }

            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_pass), true)
                false
            }

            binding.etPasswordReg.text.toString().trim {it<=' '}
                    != binding.etConfirmPassword.text.toString().trim {it<=' '} ->
            {
                showErrorSnackBar(resources.getString(R.string.err_msg_missMatch_password), true)
                false
            }
            !binding.termConditionCheckBox.isChecked ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_terms_condition), true)
                false
            }
            else ->{
                showErrorSnackBar(resources.getString(R.string.please_wait), false)
                true
            }
        }
    }

    private fun registerUser(){

        if (validateRegisterDetails()){

            showProgressDialog(resources.getString(R.string.registering_acc))

            val email: String = binding.etEmailReg.text.toString().trim { it<=' ' }
            val password: String = binding.etPasswordReg.text.toString().trim { it<=' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->


                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            binding.etFirstName.text.toString().trim { it <= ' ' },
                            binding.etLastName.text.toString().trim { it <= ' ' },
                            binding.etEmailReg.text.toString().trim { it <= ' ' },
                        )

                        showErrorSnackBar(resources.getString(R.string.msg_register_success), false)
                        Toast.makeText(
                            this@RegisterActivity,
                            resources.getString(R.string.msg_register_success), Toast.LENGTH_LONG
                        ).show()
                        FireStoreClass().registerUser(this@RegisterActivity, user)

                        FirebaseAuth.getInstance().signOut()
                        finish()
                    } else {
                        hideProgresDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userRegistrationSuccess(){
        hideProgresDialog()

        Toast.makeText(
            this@RegisterActivity, resources.getString(R.string.msg_register_success)
            ,Toast.LENGTH_LONG).show()
    }
}