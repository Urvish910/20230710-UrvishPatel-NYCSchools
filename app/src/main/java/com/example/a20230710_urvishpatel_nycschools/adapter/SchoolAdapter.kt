package com.example.a20230710_urvishpatel_nycschools.adapter


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.a20230710_urvishpatel_nycschools.R
import com.example.a20230710_urvishpatel_nycschools.databinding.ViewHolderSchoolBinding
import com.example.a20230710_urvishpatel_nycschools.model.response.SchoolResponseItem


/*
    This is a recycler view adapter using the data-binding to set the data.
    Here we using Lambda function to enable Click Listener for the view holder's btn click.
*/
class SchoolAdapter(private val list: List<SchoolResponseItem>) :
    RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder>() {
    inner class SchoolViewHolder(val binding: ViewHolderSchoolBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: SchoolResponseItem) {
            binding.data = list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewHolderSchoolBinding>(
            layoutInflater,
            R.layout.view_holder_school,
            parent,
            false
        )
        return SchoolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {

        // on click is handle and assign the value of schoolItem and position
        holder.bind(list[position])
        holder.binding.btn.setOnClickListener {
            if (this::btnlistner.isInitialized) {
                btnlistner(list[position], position)
            }
        }
        // this btnSchoolLink will handle the web site with implicit intent.
        holder.binding.btnSchoolLink.setOnClickListener {
            val context = holder.binding.root.context
            val uri =
                Uri.parse("https://data.cityofnewyork.us/resource/f9bf-2cp4.json?dbn=" + list[position].dbn.toString())
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    /*
         created custom click listener for view-holder which takes
         School-responseItem and Int for position
     */
    lateinit var btnlistner: (SchoolResponseItem, Int) -> Unit

    fun setOnClickBnt(listener: (SchoolResponseItem, Int) -> Unit) {
        btnlistner = listener
    }
}