package com.adit.bangkit.plagroid.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adit.bangkit.plagroid.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
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
}