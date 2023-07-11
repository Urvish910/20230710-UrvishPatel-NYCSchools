package com.example.a20230710_urvishpatel_nycschools.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.a20230710_urvishpatel_nycschools.R
import com.example.a20230710_urvishpatel_nycschools.adapter.SchoolAdapter
import com.example.a20230710_urvishpatel_nycschools.databinding.ActivityMainBinding
import com.example.a20230710_urvishpatel_nycschools.viewmodel.SchoolViewModel
import dagger.hilt.android.AndroidEntryPoint


/*
* MainActivity will Show the list of School and when user click on specific school for more details
* it will pass the dbn to SatSchoolActivity for showing the proper SAT score
* */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bindingUtil: ActivityMainBinding
    lateinit var schoolViewModel: SchoolViewModel
    private lateinit var adapter: SchoolAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUtil = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViewModel()                               // Function will initialize the viewModel
        initFunctionCall()                            // ViewModel's method call will be made
        observerLiveData()                            // Observing the Changes from ViewModel to UI
    }

    /*
       The function will look for changes in livedata and update ui accordingly.
       "schoolMLD" will Observe the changes and passes the list of school to adapter
       to populate the recycler view.
    */
    private fun observerLiveData() {
        schoolViewModel.progressBarMLD.observe(this) {
            if (it)
                bindingUtil.progressBar.visibility = View.VISIBLE
            else
                bindingUtil.progressBar.visibility = View.GONE
        }

        bindingUtil.data = schoolViewModel

        schoolViewModel.schoolMLD.observe(this) {
            adapter = SchoolAdapter(it)
            bindingUtil.rvSchool.adapter = adapter
            adapter.setOnClickBnt { schoolResponseItem, _ ->                             //When user clicks on the button inside the viewHolder it will pass the "dbn" to
                val intent = Intent(baseContext, SatSchoolActivity::class.java)          //SatSchoolActivity to make required network call.
                intent.putExtra("dbn", schoolResponseItem.dbn)
                startActivity(intent)
            }
        }
    }

    private fun initFunctionCall() {
        if (schoolViewModel.schoolMLD.value.isNullOrEmpty()) {          // Check if the data is empty or null.
            schoolViewModel.getSchoolList()
        }
    }

    private fun initViewModel() {
        bindingUtil.lifecycleOwner = this
        schoolViewModel = ViewModelProvider(this)[SchoolViewModel::class.java]
    }
}