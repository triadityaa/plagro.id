package com.adit.bangkit.plagroid.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    // Firebase Constants
    // This is used for the collection name for USERS.
    const val USERS: String = "users"
    const val PRODUCTS: String = "products"
    const val CART_ITEMS: String = "cart_items"
    const val ADDRESSES: String = "addresses"
    const val ORDERS: String = "orders"
    const val SOLD_PRODUCTS: String = "sold_products"

    const val PLAGRO_PREFERENCES: String = "MyShopPalPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"

    // Intent extra constants.
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"
    const val EXTRA_ADDRESS_DETAILS: String = "AddressDetails"
    const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"
    const val EXTRA_MY_ORDER_DETAILS: String = "extra_my_order_details"
    const val EXTRA_SOLD_PRODUCT_DETAILS: String = "extra_sold_product_details"

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult in the Base Activity.
    const val READ_STORAGE_PERMISSION_CODE = 2

    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val ADD_ADDRESS_REQUEST_CODE: Int = 121

    // Constant variables for Gender
    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    const val DEFAULT_CART_QUANTITY: Int = 1

    // Firebase database field names
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val USER_ID: String = "user_id"
    const val PRODUCT_ID: String = "product_id"

    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val PRODUCT_IMAGE: String = "Product_Image"

    const val CART_QUANTITY: String = "cart_quantity"

    const val STOCK_QUANTITY: String = "stock_quantity"

    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"

    //Maps
    const val GEOFENCE_RADIUS = 200
    const val GEOFENCE_ID = "REMINDER_GEOFENCE_ID"
    const val GEOFENCE_EXPIRATION = 10 * 24 * 60 * 60 * 1000 // 10 days
    const val GEOFENCE_DWELL_DELAY =  10 * 1000 // 10 secs // 2 minutes
    const val GEOFENCE_LOCATION_REQUEST_CODE = 12345
    const val CAMERA_ZOOM_LEVEL = 13f
    const val LOCATION_REQUEST_CODE = 123
    const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

    //Payment
    const val CLIENT_KEY = "SB-Mid-client-zS17TygMBI02Ta0o"
    const val STATUS_SUCCESS = "success"
    const val STATUS_FAILED = "failed"
    const val STATUS_PENDING = "pending"
    const val STATUS_CANCELLED = "cancelled"
    const val STATUS_EXPIRED = "expired"
    const val STATUS_UNKNOWN = "unknown"
    const val STATUS_PROCESSING = "processing"
    const val STATUS_REFUNDED = "refunded"
    const val STATUS_DISPUTE = "disputed"
    const val STATUS_CHARGEBACK = "chargeback"
    const val STATUS_REVERSED = "reversed"
    const val STATUS_FRAUD = "fraud"
    const val STATUS_REJECTED = "rejected"

    //sending email
    const val EMAIL_SUBJECT = "Order Confirmation"
    const val EMAIL_SENDER = "plagro.id@gmail.com"
    const val EMAIL_PASSWORD = "php2d2020"

    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}