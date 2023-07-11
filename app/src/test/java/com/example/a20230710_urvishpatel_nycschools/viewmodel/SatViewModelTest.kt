package com.example.a20230710_urvishpatel_nycschools.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.a20230710_urvishpatel_nycschools.model.response.SatResponse
import com.example.a20230710_urvishpatel_nycschools.repository.SchoolRepository
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SatViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var satResponseObserver: Observer<SatResponse>

    @Mock
    lateinit var progressBarObserver: Observer<Boolean>

    @Mock
    lateinit var messageObserver: Observer<String>


    @Mock
    lateinit var repository: SchoolRepository

    @Test
    fun `test getSatSchool() returns valid data`() {

        runTest(StandardTestDispatcher()) {
            val gson = Gson()
            val fakeResponse: Response<SatResponse> = Response.success(
                gson.fromJson(
                    TestConstants.getSatScoreResponse,
                    SatResponse::class.java
                )
            )

            Mockito.`when`(repository.getSatList("30Q501")).thenReturn(fakeResponse)

            val viewModel = SatViewModel(repository)

            viewModel.satSchoolData.observeForever(satResponseObserver)
            viewModel.progressbar.observeForever(progressBarObserver)

            viewModel.getSatSchool("30Q501")

            val expectedResult = gson.fromJson(
                TestConstants.getSatScoreResponse,
                SatResponse::class.java
            )

            verify(repository).getSatList("30Q501")

            verify(progressBarObserver).onChanged(true)
            verify(satResponseObserver).onChanged(expectedResult)
            verify(progressBarObserver).onChanged(false)

            viewModel.satSchoolData.removeObserver(satResponseObserver)
            viewModel.progressbar.removeObserver(progressBarObserver)

        }

    }

    @Test
    fun `test getSatSchool() returns error data`() {

        runTest(StandardTestDispatcher()) {
            val error = Response.error<String>(
                403,
                "Bad Request".toResponseBody("text/plan".toMediaType())
            )

            Mockito.doReturn(error).`when`(repository).getSatList("30Q501")

            val viewModel = SatViewModel(repository)

            viewModel.satSchoolData.observeForever(satResponseObserver)
            viewModel.progressbar.observeForever(progressBarObserver)
            viewModel.message.observeForever(messageObserver)

            viewModel.getSatSchool("30Q501")

            verify(repository).getSatList("30Q501")

            verify(messageObserver).onChanged("Something went wrong, Please try later.")

            viewModel.message.removeObserver(messageObserver)
            viewModel.satSchoolData.removeObserver(satResponseObserver)
            viewModel.progressbar.removeObserver(progressBarObserver)

        }

    }

    @Test(expected = RuntimeException::class)  // testing for RuntimeException.
    fun ` getSatSchool test exception`() {
        runTest(StandardTestDispatcher()) {

            // Setting up Exception to be thrown when repository.get() is executed.
            Mockito.doThrow(RuntimeException("No Internet.")).`when`(repository.getSchoolList())

            val viewModel = SatViewModel(repository)

            viewModel.message.observeForever(messageObserver)

            viewModel.getSatSchool("30Q501")

            verify(repository).getSatList("30Q501")

            val expected = RuntimeException("No internet.").toString()
            verify(messageObserver).onChanged(expected)

            viewModel.message.removeObserver(messageObserver)
        }
    }
}