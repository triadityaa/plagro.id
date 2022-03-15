package com.adit.bangkit.plagroid.ui.activity.seller.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityAddProductBinding
import com.adit.bangkit.plagroid.ui.activity.BaseActivity

class AddProductActivity : BaseActivity() {
    private lateinit var binding: ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun addProductSuccess(){
        hideProgresDialog()

        Toast.makeText(
            this@AddProductActivity, resources.getString(R.string.msg_register_success)
            , Toast.LENGTH_LONG).show()
    }
}