package com.example.a20230710_urvishpatel_nycschools.repository

import com.example.a20230710_urvishpatel_nycschools.model.response.SatResponse
import com.example.a20230710_urvishpatel_nycschools.model.response.SchoolResponse
import com.example.a20230710_urvishpatel_nycschools.model.remote.api.ApiService
import com.example.a20230710_urvishpatel_nycschools.repository.ISchoolRepository
import retrofit2.Response
import javax.inject.Inject

/*
* SchoolRepository injects ApiService and implements ISchoolRepository methods.
* */
class SchoolRepository @Inject constructor(private val apiService: ApiService): ISchoolRepository {
    override suspend fun getSchoolList(): Response<SchoolResponse> {
        return apiService.getSchoolList()
    }

    override suspend fun getSatList(dbn: String): Response<SatResponse> {
        return apiService.getSatSchool(dbn)
    }

}