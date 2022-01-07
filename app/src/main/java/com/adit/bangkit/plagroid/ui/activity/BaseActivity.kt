package com.adit.bangkit.plagroid.ui.activity

import android.app.Dialog
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adit.bangkit.plagroid.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog: Dialog

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view

        if (errorMessage){
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.red))
        }else{
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.green))
        }
        snackbar.show()
    }

    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)

        /*
        mengatur konten dari layout resource
        resource akan ......... , ditambahkan ke layer paling atas pada layar
         */
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //menampilkan dialog di layar
        mProgressDialog.show()
    }

    fun hideProgresDialog(){
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_LONG).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce = false}, 2000)
    }
}