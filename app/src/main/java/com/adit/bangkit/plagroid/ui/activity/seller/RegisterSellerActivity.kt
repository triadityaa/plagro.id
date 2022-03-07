package com.adit.bangkit.plagroid.ui.activity.seller

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityRegisterSellerBinding
import com.adit.bangkit.plagroid.firestore.FireStoreClass
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterSellerActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterSellerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSellerBinding.inflate(layoutInflater)
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

        supportActionBar?.hide()


        binding.toolbarRegisterActivity.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        binding.toolbarRegisterActivity.setNavigationOnClickListener {
            val intent = Intent(this@RegisterSellerActivity, LoginSellerActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnRegister.setOnClickListener {
            validateRegisterDetails()
            registerSeller()
        }


        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterSellerActivity, LoginSellerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    private fun validateRegisterDetails():Boolean{
        return when{
            TextUtils.isEmpty(binding.etRetailName.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmailName.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etAddressReg.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_address), true)
                false
            }

            TextUtils.isEmpty(binding.etPasswordSeller.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            binding.etPasswordSeller.length() < 8 ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password_length), true)
                false
            }

            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_pass), true)
                false
            }

            binding.etPasswordSeller.text.toString().trim {it<=' '}
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

    private fun registerSeller(){

        if (validateRegisterDetails()){

            showProgressDialog(resources.getString(R.string.registering_acc))

            val email: String = binding.etEmailName.text.toString().trim { it<=' ' }
            val password: String = binding.etPasswordSeller.text.toString().trim { it<=' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->


                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val seller = Seller(
                            firebaseUser.uid,
                            binding.etRetailName.text.toString().trim { it <= ' ' },
                            binding.etEmailName.text.toString().trim { it <= ' ' },
                            binding.etAddressReg.text.toString().trim { it <= ' ' },
                        )

                        showErrorSnackBar(resources.getString(R.string.msg_register_success), false)
                        Toast.makeText(
                            this@RegisterSellerActivity,
                            resources.getString(R.string.msg_register_success), Toast.LENGTH_LONG
                        ).show()
                        FireStoreClass().registerSeller(this@RegisterSellerActivity, seller)

                        FirebaseAuth.getInstance().signOut()
                        finish()
                    } else {
                        hideProgresDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun sellerRegistrationSuccess(){
        hideProgresDialog()

        Toast.makeText(
            this@RegisterSellerActivity, resources.getString(R.string.msg_register_success)
            , Toast.LENGTH_LONG).show()
    }
}