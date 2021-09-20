package com.adit.bangkit.plagroid.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.adit.bangkit.plagroid.activity.LoginActivity
import com.adit.bangkit.plagroid.activity.RegisterActivity
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener{
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener {e ->
                activity.hideProgresDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error While Registering User.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String{
        //Instance dari currentUser menggunakan FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //variabel yang digunakan untuk menetapkan currentUserID
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUsersDetails(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                //menerima document snapshot yang akan dikonversi ke User Data model object.
                val user = document.toObject(User::class.java)!!

                val sharedPreferenes = activity.getSharedPreferences(
                    Constants.PLAGRO_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor= sharedPreferenes.edit()
                editor.putString(
                    //key = logged_in_username
                    //value = firstname dan lastname
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )


                //START
                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }
                }
                //END
            }
            .addOnFailureListener { e ->
            when(activity){
                is LoginActivity ->{
                    activity.hideProgresDialog()
                }
            }
                Log.e(
                    activity.javaClass.simpleName,
                    e.toString()
                )
            }
    }
}