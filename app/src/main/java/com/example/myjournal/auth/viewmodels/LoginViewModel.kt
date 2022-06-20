package com.example.myjournal.auth.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.myjournal.auth.events.AuthEvent
import com.example.myjournal.auth.ui.LoginDirections
import com.example.myjournal.data.model.LoginRequest
import com.example.myjournal.data.model.RegistrationResponse
import com.example.myjournal.data.model.Resource
import com.example.myjournal.data.remote.RetrofitBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel: ViewModel() {
    val isFormValid: MutableLiveData<Boolean> = MutableLiveData(true)
    var status: MutableLiveData<Resource<*>> = MutableLiveData()
    private val api = RetrofitBuilder.gossipCentralAPI


    fun onEvent(event: AuthEvent){
        when(event){
            is AuthEvent.LoginEvent -> {

                val request = event.request
                if (
                    request.email.isEmpty() ||
                    request.password.isEmpty()
                ){
                    isFormValid.value = false
                    return
                }
                login(request
                )
            }
        }
    }

    fun login(request: LoginRequest) {
        status.value = Resource.loading(data = null);
        viewModelScope.launch(Dispatchers.IO) {
            val threadInfo = Thread.currentThread().name
            Log.i("Login", "Login running on thread $threadInfo")
            val result: RegistrationResponse?
            try {
                result = api.login(request)
                if (result.successful) {
                    Log.i("login-success", result.toString())
                    val resource = Resource.success(data = result.data)
                    status.postValue(resource)
                } else {
                    Log.i("login-failed", result.data.toString())
                    val resource = Resource.error(data = null, message = result.data.message)
                    status.postValue(resource)
                }
            } catch (e: HttpException) {
                val resource: Resource<*>
                if (e.code() == 400) {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(
                        e.response()?.errorBody()!!.charStream(),
                        RegistrationResponse::class.java
                    )
                    Log.i("register-error ", errorResponse.toString())
                    resource = Resource.error(data = null, message = errorResponse.data.message)
                    status.postValue(resource)
                } else {
                    Log.i("register-error", e.toString())
                    Log.i("register-error", e.message())
                    Log.i("register-error", e.response().toString())
                    resource = Resource.error(
                        data = null,
                        message = e.localizedMessage ?: "Looks like something went wrong"
                    )
                    status.postValue(resource)
                }

            } catch (e: Exception) {
                Log.i("Login", e.toString())
                val resource = Resource.error(
                    data = null,
                    message = e.localizedMessage ?: "Looks like something went wrong"
                )
                status.postValue(resource)
            }

        }
    }


}