package com.adit.bangkit.plagroid.ui.activity.user

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import butterknife.ButterKnife
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityDashboardBinding
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.seller.SellerProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
//        user?.let {
//            val intent = Intent(this@DashboardActivity, DashboardActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
        if (user == null){
            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@DashboardActivity,
                R.drawable.toolbar
            )
        )
        ButterKnife.bind(this)

        val navView: BottomNavigationView = binding.navView

        //val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)

        val navHostFragment =
            getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_dashboard) as NavHostFragment?
        val navController = navHostFragment!!.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_products, R.id.navigation_orders
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(this.firebaseAuthListener!!)
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}