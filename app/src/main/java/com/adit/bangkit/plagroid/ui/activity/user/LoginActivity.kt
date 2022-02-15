package com.adit.bangkit.plagroid.ui.activity.user

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityLoginBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.*
import com.adit.bangkit.plagroid.ui.activity.seller.SellerActivity
import com.adit.bangkit.plagroid.ui.admin.AdminActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var profil: SharedPreferences
    private lateinit var user: User
    private lateinit var seller: Seller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        user = User()
        seller = Seller()
        loginSession()

        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null){
            when(user.userType){
                0 -> {
                    val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@LoginActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun loginSession(){
        profil = getSharedPreferences(Constants.PLAGRO_PREFERENCES, MODE_PRIVATE)
        if (profil.getString(Constants.EXTRA_USER_DETAILS, null) != null) {
            when(user.userType){
                0 -> {
                    val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@LoginActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateLoginDetails():Boolean{
        return when{
            TextUtils.isEmpty(binding.etEmail.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else ->{
                showErrorSnackBar(resources.getString(R.string.please_wait), false)
                true
            }
        }
    }


    private fun loginRegisteredUser(){

        if (validateLoginDetails()){

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding.etEmail.text.toString().trim { it<=' ' }
            val password: String = binding.etPassword.text.toString().trim { it<=' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        showErrorSnackBar(R.string.msg_login_success.toString(), false)
                        FireStoreClass().getUsersDetails(this@LoginActivity)
                    } else {
                        hideProgresDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }


    override fun onClick(view: View?){
        if (view != null){
            when(view.id){
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    validateLoginDetails()
                    loginRegisteredUser()
                }
                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    fun userLoggedInSuccess(user: User){
        //hide progress bar
        hideProgresDialog()

        //userType 0 = admin...... userType 1 = user....... userType 2 = seller
        if (user.userType == 0){
            val intent = Intent(this@LoginActivity, AdminActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }else{
            if (user.profileComplete == 0){
                //jika profile user belum complete arahkan user ke activity UserProfileActivity
                val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                startActivity(intent)
            }else{
                //jika profile user sudah complete langsung arahkan ke MainActivity
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                startActivity(intent)
            }
            finish()
        }
    }
}