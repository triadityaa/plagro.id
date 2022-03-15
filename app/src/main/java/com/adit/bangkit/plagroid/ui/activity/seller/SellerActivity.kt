package com.adit.bangkit.plagroid.ui.activity.seller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySellerBinding
import com.adit.bangkit.plagroid.model.Product
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.seller.product.ListProductAdapter
import com.adit.bangkit.plagroid.ui.activity.user.DashboardActivity
import com.adit.bangkit.plagroid.ui.admin.AdminActivity
import com.adit.bangkit.plagroid.utils.Constants

class SellerActivity : BaseActivity() {

    private lateinit var binding: ActivitySellerBinding
    private lateinit var rvProduct: RecyclerView
    private val list = ArrayList<Product>()
    private lateinit var mSellerDetails: Seller
    private lateinit var profil: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_SELLER_DETAILS)){
            //get user details dari intent sebagai parcelable extra
            mSellerDetails = intent.getParcelableExtra(Constants.EXTRA_SELLER_DETAILS)!!
        }

        mSellerDetails = Seller()
        loginSession()



        rvProduct = findViewById(R.id.rv_products)
        rvProduct.setHasFixedSize(true)
        list.addAll(listProducts)
        showRecyclerList()
    }

    private fun loginSession(){
        profil = getSharedPreferences(Constants.PLAGRO_PREFERENCES, MODE_PRIVATE)
        if (profil.getString(Constants.EXTRA_USER_DETAILS, null) != null) {
            when(mSellerDetails.userType){
                0 -> {
                    val intent = Intent(this@SellerActivity, AdminActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mSellerDetails)
                    startActivity(intent)
                    finish()
                }
                1 -> {
                    val intent = Intent(this@SellerActivity, DashboardActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mSellerDetails)
                    startActivity(intent)
                    finish()
                }
                2 -> {
                    val intent = Intent(this@SellerActivity, SellerActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mSellerDetails)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private val listProducts: ArrayList<Product>
        get() {
            val dataName = resources.getStringArray(R.array.data_name)
            val dataDescription = resources.getStringArray(R.array.data_description)
            val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
            val listProduct = ArrayList<Product>()
            for (i in dataName.indices) {
                val product = Product(dataName[i],dataDescription[i], dataPhoto.getResourceId(i, -1))
                listProduct.add(product)
            }
            return listProduct
        }
    private fun showRecyclerList() {
        rvProduct.layoutManager = LinearLayoutManager(this)
        val listProductAdapter = ListProductAdapter(list)
        rvProduct.adapter = listProductAdapter

        listProductAdapter.setOnItemClickCallback(object : ListProductAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Product) {
               showSelectedHero(data)
            }
        })
    }

    private fun showSelectedHero(product: Product) {
        Toast.makeText(this, "Kamu memilih " + product.productName, Toast.LENGTH_SHORT).show()
    }

}