package com.adit.bangkit.plagroid.ui.activity.seller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityLoginSellerBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.user.DashboardActivity
import com.adit.bangkit.plagroid.ui.activity.user.ForgotPasswordActivity
import com.adit.bangkit.plagroid.ui.admin.AdminActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginSellerActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginSellerBinding
    private lateinit var profil: SharedPreferences
    private lateinit var user: User
    private lateinit var seller: Seller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSellerBinding.inflate(layoutInflater)
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
            when(seller.userType){
                0 -> {
                    val intent = Intent(this@LoginSellerActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@LoginSellerActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@LoginSellerActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun loginSession(){
        profil = getSharedPreferences(Constants.PLAGRO_PREFERENCES, MODE_PRIVATE)
        if (profil.getString(Constants.EXTRA_USER_DETAILS, null) != null) {
            when(seller.userType){
                0 -> {
                    val intent = Intent(this@LoginSellerActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@LoginSellerActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@LoginSellerActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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


    private fun loginRegisteredSeller(){

        if (validateLoginDetails()){

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding.etEmail.text.toString().trim { it<=' ' }
            val password: String = binding.etPassword.text.toString().trim { it<=' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        showErrorSnackBar(R.string.msg_login_success.toString(), false)
                        FireStoreClass().getSellersDetails(this@LoginSellerActivity)
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
                    val intent = Intent(this@LoginSellerActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    validateLoginDetails()
                    loginRegisteredSeller()
                }
                R.id.tv_register -> {
                    val intent = Intent(this@LoginSellerActivity, RegisterSellerActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    fun sellerLoggedInSuccess(seller: User){
        //hide progress bar
        hideProgresDialog()

            if (seller.profileComplete == 0){
                //jika profile user belum complete arahkan user ke activity UserProfileActivity
                val intent = Intent(this@LoginSellerActivity, SellerProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }else{
                //jika profile user sudah complete langsung arahkan ke MainActivity
                val intent = Intent(this@LoginSellerActivity, SellerActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELLER_DETAILS, seller)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
            finish()
        }
    }
