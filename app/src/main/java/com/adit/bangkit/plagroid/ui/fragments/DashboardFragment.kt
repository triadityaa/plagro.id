package com.adit.bangkit.plagroid.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.FragmentDashboardBinding
import com.adit.bangkit.plagroid.ui.activity.SettingsActivity
import com.adit.bangkit.plagroid.ui.activity.WishlistActivity

class DashboardFragment : Fragment() {


    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textDashboard
        textView.text =  "this is home fragment"
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