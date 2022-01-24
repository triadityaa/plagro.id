package com.adit.bangkit.plagroid.ui.activity.user

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityForgotPasswordBinding
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.captionBar())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.toolbarForgotPasswordActivity.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbarForgotPasswordActivity.setNavigationOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val email: String = binding.etEmailForgot.text.toString().trim { it<=' ' }
            if (email.isEmpty()){
                showErrorSnackBar(R.string.err_msg_forgot_password.toString(), true)
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        hideProgresDialog()
                        if (task.isSuccessful){
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                resources.getString(R.string.forgot_password_success),
                                Toast.LENGTH_LONG).show()

                            finish()
                        }else{
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }

}