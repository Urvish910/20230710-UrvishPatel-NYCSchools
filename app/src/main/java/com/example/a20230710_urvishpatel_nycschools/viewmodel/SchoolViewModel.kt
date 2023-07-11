package com.example.a20230710_urvishpatel_nycschools.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a20230710_urvishpatel_nycschools.model.response.SchoolResponse
import com.example.a20230710_urvishpatel_nycschools.repository.ISchoolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/* SchoolViewModel is having data, message, and progressbar as a mutable live data
 to update the value accordingly. we are using the "Coroutine" to make sure that
 operation is still running even the screen is changes has accused.
 Making call the repository to get the data as posting the values into the mutable live data.
 */
@HiltViewModel
class SchoolViewModel @Inject constructor(val repository: ISchoolRepository) : ViewModel() {

    val schoolMLD = MutableLiveData<SchoolResponse>()
    val messageMLD = MutableLiveData<String>()
    val progressBarMLD = MutableLiveData<Boolean>()

    fun getSchoolList() {
        progressBarMLD.postValue(true)
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            messageMLD.postValue(throwable.message)
            return@CoroutineExceptionHandler
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = repository.getSchoolList()
            if (!response.isSuccessful) {
                messageMLD.postValue("Something Went Wrong, Please try later.")
                progressBarMLD.postValue(false)
                return@launch
            } else if (response.body().isNullOrEmpty()) {
                messageMLD.postValue("Data Not Found")
                progressBarMLD.postValue(false)
                return@launch
            }
            schoolMLD.postValue(response.body())
            progressBarMLD.postValue(false)
            return@launch
        }
    }
}