package com.adit.bangkit.plagroid.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.seller.LoginSellerActivity
import com.adit.bangkit.plagroid.ui.activity.seller.RegisterSellerActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerProfileActivity
import com.adit.bangkit.plagroid.ui.activity.user.LoginActivity
import com.adit.bangkit.plagroid.ui.activity.user.RegisterActivity
import com.adit.bangkit.plagroid.ui.activity.user.SettingsActivity
import com.adit.bangkit.plagroid.ui.activity.user.UserProfileActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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


    fun registerSeller(activity: RegisterSellerActivity, sellerInfo: Seller){

        mFirestore.collection(Constants.SELLER)
            .document(sellerInfo.id)
            .set(sellerInfo, SetOptions.merge())
            .addOnSuccessListener{
                activity.sellerRegistrationSuccess()
            }
            .addOnFailureListener {e ->
                activity.hideProgresDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error While Registering Seller.",
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
        if (getCurrentUserID().isNotEmpty()){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                //menerima document snapshot yang akan dikonversi ke User Data model object.
                val user = document.toObject(User::class.java)!!
                val username = " " + user.firstName + " " + user.lastName

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.PLAGRO_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor= sharedPreferences.edit()
                editor.putString(
                    //key = logged_in_username
                    //value = firstname dan lastname
                    Constants.LOGGED_IN_USERNAME,
                    username
                )
                editor.apply()


                //START
                when(activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.loadUserDetailsSuccess(user)
                    }
                }
                //END
            }
            .addOnFailureListener { e ->
            when(activity){
                is LoginActivity ->{
                    activity.hideProgresDialog()
                }
                is SettingsActivity -> {
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



    fun getSellersDetails(activity: Activity){
        if (getCurrentUserID().isNotEmpty()){
            mFirestore.collection(Constants.SELLER)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener {document ->
                    Log.i(activity.javaClass.simpleName, document.toString())

                    //menerima document snapshot yang akan dikonversi ke User Data model object.
                    val seller = document.toObject(Seller::class.java)!!
                    val retailName = " " + seller.retailName

                    val sharedPreferences = activity.getSharedPreferences(
                        Constants.PLAGRO_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                    val editor: SharedPreferences.Editor= sharedPreferences.edit()
                    editor.putString(
                        //key = logged_in_username
                        //value = firstname dan lastname
                        Constants.LOGGED_IN_RETAILNAME,
                        retailName
                    )
                    editor.apply()


                    //START
                    when(activity){
                        is LoginSellerActivity -> {
                            activity.sellerLoggedInSuccess(seller)
                        }
                        is SellerProfileActivity -> {
//                            activity.loadUserDetailsSuccess(user)
                        }
                    }
                    //END
                }
                .addOnFailureListener { e ->
                    when(activity){
                        is SettingsActivity ->{
                            activity.hideProgresDialog()
                        }
                        is SellerProfileActivity -> {
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

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                    activity.hideProgresDialog()
                }
            }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details",
                    e
                )
            }
    }



    fun updateSellerProfileData(activity: Activity, sellerHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.SELLER).document(getCurrentUserID())
            .update(sellerHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is SellerProfileActivity -> {
                        activity.sellerProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is SellerProfileActivity -> {
                        activity.hideProgresDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, ImageFileURI: Uri?){
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
        + Constants.getFileExtension(
                activity, ImageFileURI
           )
        )

        storageRef.putFile(ImageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e(
                "firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when(activity){
                        is UserProfileActivity ->{
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is SellerProfileActivity ->{
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }

        }
            .addOnFailureListener { exception ->
                when(activity){
                    is UserProfileActivity ->{
                        activity.hideProgresDialog()
                    }
                    is SellerProfileActivity ->{
                        activity.hideProgresDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
}