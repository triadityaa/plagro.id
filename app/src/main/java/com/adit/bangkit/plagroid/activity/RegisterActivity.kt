package com.adit.bangkit.plagroid.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
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

//        AlertDialog.Builder(this)
//            .setTitle("Abort Register")
//            .setMessage("Are You Sure?")
//            .setNegativeButton(R.string.no, null)
//            .setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener{
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    TODO("Not yet implemented")
//                }
//
//            })


        binding.toolbarRegisterActivity.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbarRegisterActivity.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnRegister.setOnClickListener {
            validateRegisterDetails()
            RegisterUser()
        }


        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
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

            binding.etPasswordReg.length() <= 8 ->{
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
                showErrorSnackBar(resources.getString(R.string.data_complete), false)
                true
            }
        }
    }

    private fun RegisterUser(){

        if (validateRegisterDetails()){

            showProgressDialog()

            val email: String = binding.etEmailReg.text.toString().trim { it<=' ' }
            val password: String = binding.etPasswordReg.text.toString().trim { it<=' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                    hideProgresDialog()

                    if (task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        showErrorSnackBar(R.string.msg_register_success.toString() , false)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                })
        }
    }
}