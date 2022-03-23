package com.adit.bangkit.plagroid.ui.activity.seller.product

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityAddProductBinding
import com.adit.bangkit.plagroid.model.Product
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.utils.Constants
import com.adit.bangkit.plagroid.utils.GlideLoader
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class AddProductActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var mProductDetails: Product
    private lateinit var mSellerDetails: Seller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mProductDetails = Product()
        mSellerDetails = Seller()

        binding.btnSubmitProduct.setOnClickListener(this)
    }


    fun loadProductsDetailsSuccess(product: Product) {
        mProductDetails = product
        hideProgresDialog()

        GlideLoader(this@AddProductActivity).loadUserPicture(
            product.productPict,
            binding.tvProductImageSetting
        )
        binding.etProductName.setText(product.productName)
        binding.etCategoryName.setText(product.category)
        binding.etProductPrice.setText(product.productPrice.toString())
        binding.etProductStock.setText(product.productStock.toString())

    }


    private fun validateProductDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etProductName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etCategoryName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etProductStock.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.please_wait), false)
                true
            }
        }
    }


    private fun addProduct(productInfo: Product, sellerInfo: Seller) {

        if (validateProductDetails()) {

            showProgressDialog(resources.getString(R.string.adding_product))


            val db = FirebaseFirestore.getInstance()
            val product: MutableMap<String, Any> = HashMap()

            db.collection(Constants.SELLER)
                .document(sellerInfo.id)
                .collection(Constants.CATEGORY)
                .document(productInfo.category)
                .collection(Constants.PRODUCT)
                .document(productInfo.id).set(product, SetOptions.merge())
                .addOnSuccessListener {
                hideProgresDialog()
                showErrorSnackBar(resources.getString(R.string.product_add_success), false)
                finish()
            }.addOnFailureListener {
                hideProgresDialog()
                showErrorSnackBar(resources.getString(R.string.product_add_failed), true)
            }
        }
    }

    fun addProductSuccess() {
        hideProgresDialog()

        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.msg_register_success),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_submit_product -> {
                    addProduct(mProductDetails, mSellerDetails)
                }
            }
        }
    }
}