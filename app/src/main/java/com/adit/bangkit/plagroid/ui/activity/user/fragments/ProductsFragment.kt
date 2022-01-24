package com.adit.bangkit.plagroid.ui.activity.user.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.FragmentProductsBinding
import com.adit.bangkit.plagroid.ui.activity.user.SettingsActivity
import com.adit.bangkit.plagroid.ui.activity.user.WishlistActivity


class ProductsFragment : Fragment() {

    //private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textHome
        textView.text = "this is products fragment"
        val progressBar: ProgressBar = binding.progressBar
        progressBar.visibility=

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings ->{
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }

            R.id.wishlist -> {
                startActivity(Intent(activity, WishlistActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}