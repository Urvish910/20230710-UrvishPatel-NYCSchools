package com.example.a20230710_urvishpatel_nycschools.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.a20230710_urvishpatel_nycschools.databinding.ActivitySatSchoolBinding
import com.example.a20230710_urvishpatel_nycschools.viewmodel.SatViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
* SatSchoolActivity is getting the data (dbn) from MainActivity to show the
* school details by pass the dbn to Sat-view-model.
* */
@AndroidEntryPoint
class SatSchoolActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySatSchoolBinding
    lateinit var viewModel: SatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySatSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        intiFunctionCAll()
        intiObserver()
    }

    // check and see if the data is present else show the error message on the screen and stop the progress bar loading.
    @SuppressLint("SetTextI18n")
    private fun intiObserver() {
        viewModel.satSchoolData.observe(this) {
            if (!it.isEmpty()) {
                binding.data = it[0]
            } else {
                binding.tvSchoolName.text = "Sorry No Data is Available. Please try later"
            }
        }
        viewModel.progressbar.observe(this) {

            if (it)
                binding.progressBarCheck.visibility = View.VISIBLE
            else
                binding.progressBarCheck.visibility = View.GONE
        }
    }

    private fun intiFunctionCAll() {
        val dbn = intent.getStringExtra("dbn")
        if (dbn != null) {
            viewModel.getSatSchool(dbn)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[SatViewModel::class.java]
    }
}