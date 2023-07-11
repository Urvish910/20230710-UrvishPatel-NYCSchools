package com.example.a20230710_urvishpatel_nycschools.model.remote.api

import com.example.a20230710_urvishpatel_nycschools.model.response.SatResponse
import com.example.a20230710_urvishpatel_nycschools.model.response.SchoolResponse
import com.example.a20230710_urvishpatel_nycschools.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
/*
* ApiService is used to make the network call respective
* */
interface ApiService {
    @GET(Constants.END_POINT_SCHOOL)
    suspend fun getSchoolList(): Response<SchoolResponse>

    @GET(Constants.SAT_END_URL)
    suspend fun getSatSchool(@Query("dbn") dbn: String): Response<SatResponse>
}