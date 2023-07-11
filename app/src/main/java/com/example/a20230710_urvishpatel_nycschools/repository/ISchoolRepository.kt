package com.example.a20230710_urvishpatel_nycschools.repository

import com.example.a20230710_urvishpatel_nycschools.model.response.SatResponse
import com.example.a20230710_urvishpatel_nycschools.model.response.SchoolResponse
import retrofit2.Response

/*
* Using the RepositoryPattern to separating the data from Remote
* and local database.
* */
interface ISchoolRepository {
    suspend fun getSchoolList() : Response<SchoolResponse>
    suspend fun getSatList(dbn:String) : Response<SatResponse>
}