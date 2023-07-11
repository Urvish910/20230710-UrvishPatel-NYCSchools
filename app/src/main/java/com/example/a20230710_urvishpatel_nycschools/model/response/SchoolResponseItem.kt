package com.example.a20230710_urvishpatel_nycschools.model.response

data class SchoolResponseItem(
    val city: String,
    val dbn: String,
    val phone_number: String,
    val school_email: String,
    val school_name: String,
    val total_students: String,
    val zip: String
)