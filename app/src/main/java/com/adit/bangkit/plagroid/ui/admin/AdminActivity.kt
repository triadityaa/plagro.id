package com.adit.bangkit.plagroid.ui.admin

import android.os.Bundle
import com.adit.bangkit.plagroid.databinding.ActivityAdminBinding
import com.adit.bangkit.plagroid.ui.activity.BaseActivity

class AdminActivity : BaseActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}