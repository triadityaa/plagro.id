package com.plagro.id.admin.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.plagro.id.admin.R
import com.plagro.id.admin.databinding.ActivityAddProductBinding
import com.plagro.id.admin.firestore.FirestoreClass
import com.plagro.id.admin.models.Product
import com.plagro.id.admin.utils.Constants
import com.plagro.id.admin.utils.GlideLoader
import java.io.IOException

/**
 * Add Product screen of the app.
 */
class AddProductActivity : BaseActivity(), View.OnClickListener {

    // A global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var mProductDetails: Product

    // A global variable for uploaded product image URL.
    private var mProductImageURL: String = ""
    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            // Get the user details from intent as a StringeExtra.
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mProductDetails = intent.getParcelableExtra(Constants.EXTRA_PRODUCT_DETAILS)!!
        }

        if (intent.hasExtra(Constants.PRODUCT_IMAGE)){
            mProductImageURL = intent.getStringExtra(Constants.PRODUCT_IMAGE)!!
        }

        var productOwnerId = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirestoreClass().getCurrentUserID() == productOwnerId) {
            binding.tvTitle.text = resources.getString(R.string.update_product).uppercase()

            // Load the image using the GlideLoader class with the use of Glide Library.
            GlideLoader(this@AddProductActivity).loadUserPicture(mProductDetails.image, binding.ivProductImage)

            // Set the existing values to the UI and allow user to edit except the Email ID.
            binding.etProductTitle.setText(mProductDetails.title)

            binding.etProductPrice.setText(mProductDetails.price)
            binding.etProductDescription.setText(mProductDetails.description)
            binding.etProductQuantity.setText(mProductDetails.stock_quantity)
//            binding.btnUpdate.text = resources.getString(R.string.update_product).uppercase()
//            binding.btnUpdate.setOnClickListener {
//                if (validateUpdateProductDetails()) {
//
//                    showProgressDialog()
//
//                    if (mSelectedImageFileUri == mProductImageURL.toUri()) {
//
//                        FirestoreClass().uploadImageToCloudStorage(
//                            this@AddProductActivity,
//                            mSelectedImageFileUri,
//                            Constants.PRODUCT_IMAGE
//                        )
//                    } else {
//
//                        if (mSelectedImageFileUri == mProductImageURL.toUri()) {
//
//                            FirestoreClass().uploadImageToCloudStorage(
//                                this@AddProductActivity,
//                                mProductImageURL.toUri(),
//                                Constants.PRODUCT_IMAGE
//                            )
//                        }
//                        uploadProductDetails()
//                        updateProductsDetails()
//
//                        startActivity(Intent(this@AddProductActivity, DashboardActivity::class.java))
//                        finish()
//                    }
//                }
//            }
        }

        setupActionBar()

        // Assign the click event to iv_add_update_product image.
        binding.ivAddUpdateProduct.setOnClickListener(this)

        // Assign the click event to submit button.
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                // The permission code is similar to the user profile image selection.
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@AddProductActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    if (validateProductDetails()) {

                        uploadProductImage()
                    }else{
//                        uploadProductDetails()
                        updateProductsDetails()

                        val intent= (Intent(this@AddProductActivity, DashboardActivity::class.java))
                        intent.putExtra(Constants.EXTRA_PRODUCT_DETAILS, mProductDetails)
                        intent.putExtra(Constants.EXTRA_PRODUCT_ID, mProductId)
                        intent.putExtra(Constants.PRODUCT_IMAGE, mProductImageURL)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddProductActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this@AddProductActivity).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.ivProductImage
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddProductActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddProductActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddProductActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to validate the product details.
     */
    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                false
            }

            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_quantity),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }




    private fun validateUpdateProductDetails(): Boolean {
        return when {

//            mSelectedImageFileUri == null -> {
//                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
//                false
//            }

            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_quantity),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * A function to upload the selected product image to firebase cloud storage.
     */
    private fun uploadProductImage() {

        showProgressDialog()

        FirestoreClass().uploadImageToCloudStorage(
            this@AddProductActivity,
            mSelectedImageFileUri,
            Constants.PRODUCT_IMAGE
        )
    }

    /**
     * A function to get the successful result of product image upload.
     */
    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        mProductImageURL = imageURL

        uploadProductDetails()
    }

    private fun uploadProductDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val product = Product(
            FirestoreClass().getCurrentUserID(),
            username,
            binding.etProductTitle.text.toString().trim { it <= ' ' },
            binding.etProductPrice.text.toString().trim { it <= ' ' },
            binding.etProductDescription.text.toString().trim { it <= ' ' },
            binding.etProductQuantity.text.toString().trim { it <= ' ' },
            mProductImageURL
        )
//        FirestoreClass().deleteProductOnUpdate(this@AddProductActivity, mProductId)

        FirestoreClass().uploadProductDetails(this@AddProductActivity, product)
    }

    fun productDeleteSuccess(){
        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText( this,
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * A function to return the successful result of Product upload.
     */
    fun productUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun updateProductsDetails() {

        val itemHashMap = HashMap<String, Any>()

        // Get the FirstName from editText and trim the space
        val productName = binding.etProductTitle.text.toString().trim { it <= ' ' }
        if (productName != mProductDetails.title) {
            itemHashMap[Constants.PRODUCT_NAME] = productName
        }

        // Here we get the text from editText and trim the space
        val productPrice = binding.etProductPrice.text.toString().trim { it <= ' ' }


        if (mProductImageURL.isNotEmpty()) {
            itemHashMap[Constants.PRODUCT_IMAGE] = mProductImageURL
        }

        if (productPrice.isNotEmpty() && productPrice != mProductDetails.price) {
            itemHashMap[Constants.PRODUCT_PRICE] = productPrice
        }

        val productDescription = binding.etProductDescription.text.toString().trim { it <= ' ' }
        if (productDescription.isNotEmpty() && productDescription != mProductDetails.description) {
            itemHashMap[Constants.PRODUCT_DESCRIPTION] = productDescription
        }

        val productStock = binding.etProductQuantity.text.toString().trim { it <= ' ' }
        if (productStock.isNotEmpty() && productStock != mProductDetails.stock_quantity) {
            itemHashMap[Constants.STOCK_QUANTITY] = productStock
        }

//        FirestoreClass().deleteProductOnUpdate(this@AddProductActivity, mProductId)

        // call the registerUser function of FireStore class to make an entry in the database.
        FirestoreClass().updateProductData(
            this@AddProductActivity, mProductId, itemHashMap
        )
    }

    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun productUpdateSuccess() {

//        // Hide the progress dialog
//        hideProgressDialog()

        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.msg_product_update_success),
            Toast.LENGTH_SHORT
        ).show()
    }
}