package com.plagro.id.admin.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.plagro.id.admin.R
import com.plagro.id.admin.databinding.ActivitySoldReportBinding
import com.plagro.id.admin.firestore.FirestoreClass
import com.plagro.id.admin.models.SoldProduct
import com.plagro.id.admin.ui.adapters.SoldProductsListAdapter

class SoldReportActivity : BaseActivity() {

    private lateinit var binding: ActivitySoldReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        getSoldProductsList()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSalesReportActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarSalesReportActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getSoldProductsList() {
        // Show the progress dialog.
        showProgressDialog()

        // Call the function of Firestore class.
        FirestoreClass().getSoldProductsList(this@SoldReportActivity)
    }

    /**
     * A function to get the list of sold products.
     */
    fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (soldProductsList.size > 0) {
            binding.rvSoldProductItems.visibility = View.VISIBLE
            binding.tvNoSoldProductsFound.visibility = View.GONE

            binding.rvSoldProductItems.layoutManager = LinearLayoutManager(this@SoldReportActivity)
            binding.rvSoldProductItems.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductsListAdapter(this@SoldReportActivity, soldProductsList)
            binding.rvSoldProductItems.adapter = soldProductsListAdapter
        } else {
            binding.rvSoldProductItems.visibility = View.GONE
            binding.tvNoSoldProductsFound.visibility = View.VISIBLE
        }
    }
}