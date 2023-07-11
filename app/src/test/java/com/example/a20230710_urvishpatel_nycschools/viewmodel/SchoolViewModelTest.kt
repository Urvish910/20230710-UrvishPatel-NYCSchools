package com.example.a20230710_urvishpatel_nycschools.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.a20230710_urvishpatel_nycschools.model.response.SchoolResponse
import com.example.a20230710_urvishpatel_nycschools.repository.SchoolRepository
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SchoolViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var schoolResponseObserver: Observer<SchoolResponse>

    @Mock
    lateinit var progressBarObserver: Observer<Boolean>

    @Mock
    lateinit var messageObserver: Observer<String>



    @Mock
    lateinit var repository: SchoolRepository

    @Test
    fun `test getSchool() returns valid data`() {

        runTest(StandardTestDispatcher()) {
            val gson = Gson()
            val fakeResponse: Response<SchoolResponse> = Response.success(
                gson.fromJson(
                    TestConstants.getallSchoolResponse,
                    SchoolResponse::class.java)
            )

            Mockito.`when`(repository.getSchoolList()).thenReturn(fakeResponse)

            val viewModel = SchoolViewModel(repository)

            viewModel.schoolMLD.observeForever(schoolResponseObserver)
            viewModel.progressBarMLD.observeForever(progressBarObserver)

            viewModel.getSchoolList()

            val expectedResult = gson.fromJson(
                TestConstants.getallSchoolResponse,
                SchoolResponse::class.java
            )

            verify(repository).getSchoolList()

            verify(progressBarObserver).onChanged(true)
            verify(schoolResponseObserver).onChanged(expectedResult)
            verify(progressBarObserver).onChanged(false)

            viewModel.schoolMLD.removeObserver(schoolResponseObserver)
            viewModel.progressBarMLD.removeObserver(progressBarObserver)

        }

    }

    @Test
    fun `test getSchool() returns error data`() {

        runTest(StandardTestDispatcher()) {
            val error = Response.error<String>(403, ResponseBody.create("text/plan".toMediaType(), "Bad Request"))

            doReturn(error).`when`(repository).getSchoolList()

            val viewModel = SchoolViewModel(repository)

            viewModel.schoolMLD.observeForever(schoolResponseObserver)
            viewModel.progressBarMLD.observeForever(progressBarObserver)


            viewModel.messageMLD.observeForever(messageObserver)

            viewModel.getSchoolList()

            verify(repository).getSchoolList()

            verify(messageObserver).onChanged("Something Went Wrong, Please try later.")

            viewModel.messageMLD.removeObserver(messageObserver)

        }

    }

    @Test(expected = RuntimeException::class)  // testing for RuntimeException.
    fun `test exception`() {
        runTest(StandardTestDispatcher()) {

            // Setting up Exception to be thrown when repository.getRandomFact() is executed.
            doThrow(RuntimeException("No Internet.")).`when`(repository.getSchoolList())

            val viewModel = SchoolViewModel(repository)

            viewModel.messageMLD.observeForever(messageObserver)

            viewModel.getSchoolList()

            verify(repository).getSchoolList()

            val expected = RuntimeException("No internet.").toString()
            verify(messageObserver).onChanged(expected)

            viewModel.messageMLD.removeObserver(messageObserver)
        }
    }
}