package com.awaken.appm.android.navigation.viewmodels

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.awaken.appm.android.helpers.MonthNameHelper
import com.awaken.appm.android.navigation.Routes
import com.awaken.appm.android.network.SheetApiClient
import com.awaken.appm.android.repository.SharedPrefsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar
import java.util.Locale

class AddSheetViewModel : ViewModel() {

    val linkText: MutableStateFlow<String> = MutableStateFlow("")

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown: StateFlow<Boolean> = _isDialogShown

    private fun showDialog() {
        _isDialogShown.value = true
    }

    private fun hideDialog() {
        _isDialogShown.value = false
    }

    suspend fun trySaveLink(context: Context, navController: NavController) {

        val link = linkText.value.replace("\n","").replace(" ", "").replace("\t", "")

        if (link.isNotEmpty()) {

            showDialog()

            val response = try {
                SheetApiClient.apiService.getMoney(link, MonthNameHelper().getCurrentMonthName(), 2, 13, 1, 1)
            } catch (e: IOException) {
                delay(1000L)
                _toastMessage.value = "Проверьте подключение к интернету"
                hideDialog()
                null
            } catch (e: HttpException) {
                _toastMessage.value = "Проверьте правильность ссылки: " + e.code()
                hideDialog()
                null
            } catch (e: Exception){
                _toastMessage.value = "Проверьте правильность таблицы"
                hideDialog()
                null
            }

            if (response != null) {
                if (response.isSuccessful) {
                    val responseModel = response.body()
                    if (responseModel?.error != null) {
                        _toastMessage.value = "Проверьте правильность ссылки: " + responseModel.error
                        hideDialog()
                    } else {

                        hideDialog()
                        val sharedPreferencesHelper = SharedPrefsHelper(context)
                        sharedPreferencesHelper.setLink(link)
                        runOnUiThread{navController.navigate(Routes.Home.route)}
                        _toastMessage.value = "Таблица успешно сохранена"
                    }
                } else {
                    _toastMessage.value = "Проверьте правильность ссылки: " + response.code()
                    hideDialog()
                }
            }
        } else{
            _toastMessage.value = "Поле не может быть пустым"
        }
    }

    fun cleanToastMessage(){
        _toastMessage.value = null
    }

    private fun runOnUiThread(runnable: Runnable) {
        val looper = Looper.getMainLooper()
        if (Thread.currentThread() === looper.thread) {
            runnable.run()
        } else {
            Handler(looper).post(runnable)
        }
    }
}