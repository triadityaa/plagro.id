package com.adit.bangkit.plagroid.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    //user
    const val USERS: String = "users"

    //seller
    const val SELLER: String = "seller"
    const val RETAIL_NAME: String = "retail_name"

    //const untuk user dan seller
    const val PLAGRO_PREFERENCES: String = "PlagroPref"
    const val LOGGED_IN_RETAILNAME: String = "logged_in_retailname"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val EXTRA_SELLER_DETAILS: String = "extra_seller_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val FIRST_NAME: String = "firstname"
    const val LAST_NAME: String = "lastname"
    const val EMAIL: String = "email"
    const val MOBILE: String = "mobile"
    const val ADDRESS: String = "address"
    const val GENDER: String = "gender"
    const val CODEPOS: String = "kodepos"
    const val COMPLETE_PROFILE: String = "profileComplete"
    const val IMAGE: String = "image"
    const val USER_PROFILE_IMAGE: String = "user_profile_image"

    fun showImageChooser(activity: Activity){
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intentGallery, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, Uri: Uri?): String? {

        //MimetypeMap = tow-way map that maps Mime-types to file extensions and vice versa.

        //getSingleton = get the Singleton instance of MimetypeMap.

        //getExtensionFromMimeType = return the registered extension for the given MIME type

        //contentResolver.getType = return the MIME type of the given content URL
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(Uri!!))
    }
}