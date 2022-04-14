package com.plagro.id.admin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.plagro.id.admin.R
import com.plagro.id.admin.databinding.FragmentSoldProductsBinding
import com.plagro.id.admin.firestore.FirestoreClass
import com.plagro.id.admin.models.SoldProduct
import com.plagro.id.admin.ui.adapters.SoldProductsListAdapter
import com.plagro.id.admin.ui.fragments.BaseFragment

/**
 * Sold Products Listing Fragment.
 */
class SoldProductsFragment : BaseFragment() {
    private lateinit var binding: FragmentSoldProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSoldProductsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
//        return
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        getSoldProductsList()
    }

    private fun getSoldProductsList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getSoldProductsList(this@SoldProductsFragment)
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

            binding.rvSoldProductItems.layoutManager = LinearLayoutManager(activity)
            binding.rvSoldProductItems.setHasFixedSize(true)

            val soldProductsListAdapter =
                SoldProductsListAdapter(requireActivity(), soldProductsList)
            binding.rvSoldProductItems.adapter = soldProductsListAdapter
        } else {
            binding.rvSoldProductItems.visibility = View.GONE
            binding.tvNoSoldProductsFound.visibility = View.VISIBLE
        }
    }
}