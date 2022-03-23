package com.adit.bangkit.plagroid.ui.activity.seller

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySellerBinding
import com.adit.bangkit.plagroid.model.Product
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.model.User
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.seller.product.AddProductActivity
import com.adit.bangkit.plagroid.ui.activity.seller.product.ListProductAdapter
import com.adit.bangkit.plagroid.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class SellerActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySellerBinding
    private lateinit var rvProduct: RecyclerView
    private lateinit var listProductAdapter: ListProductAdapter
    private var productArrayList = ArrayList<Product>()
    private lateinit var mSellerDetails: Seller
    private lateinit var user: User
    private lateinit var profil: SharedPreferences

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val seller = firebaseAuth.currentUser?.uid
        if (seller == null){
            val intent = Intent(this@SellerActivity, LoginSellerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_SELLER_DETAILS)){
            //get user details dari intent sebagai parcelable extra
            mSellerDetails = intent.getParcelableExtra(Constants.EXTRA_SELLER_DETAILS)!!
        }

        user = User()
        mSellerDetails = Seller()
        loginSession()



//        rvProduct = findViewById(R.id.rv_products)
//        rvProduct.layoutManager = LinearLayoutManager(this)
//        rvProduct.setHasFixedSize(true)
//
//        productArrayList = arrayListOf()
//        listProductAdapter = ListProductAdapter(productArrayList)
//        rvProduct.adapter = listProductAdapter
//        productArrayList.addAll(listProducts)
//        showRecyclerList()

        binding.btnAddProduct.setOnClickListener(this)
    }



    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(this.firebaseAuthListener!!)
            }


    private fun loginSession(){
        profil = getSharedPreferences(Constants.PLAGRO_PREFERENCES, MODE_PRIVATE)
        if (profil.getString(Constants.EXTRA_SELLER_DETAILS, null) != null) {
                    if (mSellerDetails.userType == 2){
                        val intent = Intent(this@SellerActivity, SellerActivity::class.java)
                        intent.putExtra(Constants.EXTRA_SELLER_DETAILS, mSellerDetails)
                        startActivity(intent)
                        finish()
                    }
                }
            }




    override fun onClick(view: View?){
        if (view != null){
            when(view.id){
                R.id.btn_add_product -> {
                    val intent = Intent(this@SellerActivity, AddProductActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showProductList() {
    }
//    private val listProducts: ArrayList<Product>
//        get() {
//            val dataName = resources.getStringArray(R.array.data_name)
//            val dataDescription = resources.getStringArray(R.array.data_description)
//            val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
//            val listProduct = ArrayList<Product>()
//            for (i in dataName.indices) {
//                val product = Product(dataName[i],dataDescription[i], dataPhoto.getResourceId(i, -1))
//                listProduct.add(product)
//            }
//            return listProduct
//        }

    private fun showRecyclerList() {
        rvProduct.layoutManager = LinearLayoutManager(this)
        val listProductAdapter = ListProductAdapter(productArrayList)
        rvProduct.adapter = listProductAdapter

        listProductAdapter.setOnItemClickCallback(object : ListProductAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Product) {
               showSelectedProduct(data)
            }
        })
    }

    private fun showSelectedProduct(product: Product) {
        Toast.makeText(this, "Kamu memilih " + product.productName, Toast.LENGTH_SHORT).show()
    }

}