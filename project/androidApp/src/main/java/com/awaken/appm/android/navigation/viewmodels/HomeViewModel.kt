package com.awaken.appm.android.navigation.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.awaken.appm.android.helpers.MonthNameHelper
import com.awaken.appm.android.navigation.Routes
import com.awaken.appm.android.network.SheetApiClient
import com.awaken.appm.android.repository.SharedPrefsHelper
import com.awaken.appm.android.repository.ThemesTypes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(val context: Context) : ViewModel() {

    enum class ButtonState { DOHOD, RASHOD, KREDIT }

    val selectedButton = MutableStateFlow(ButtonState.RASHOD)

    val selectedCategory = MutableStateFlow("")

    val currentDate = MutableStateFlow(LocalDate.now())

    val currentMoney = MutableStateFlow("")

    val currentComment = MutableStateFlow("")

    val moneyFromSheet = MutableStateFlow(0)
    val categoriesDohodList = MutableStateFlow(emptyList<String>())
    val categoriesRashodList = MutableStateFlow(emptyList<String>())
    val listForCategories = MutableStateFlow(emptyList<String>())

    val isInternetAvailable = MutableStateFlow(isOnline(context))

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage
    fun cleanToastMessage() {
        _toastMessage.value = null
    }

    private val _isDialogShown = MutableStateFlow(false)
    val isDialogShown: StateFlow<Boolean> = _isDialogShown

    private fun showDialog() {
        _isDialogShown.value = true
    }

    private fun hideDialog() {
        _isDialogShown.value = false
    }

    fun resetLink(context: Context, navController: NavController) {
        val sharePref = SharedPrefsHelper(context)
        sharePref.deleteLink()
        navController.navigate(Routes.AddSheet.route)
    }

    fun changeTheme(context: Context, isSystemTheme: Boolean) {
        val sharePref = SharedPrefsHelper(context)
        val theme = sharePref.getTheme(isSystemTheme)

        if (theme == ThemesTypes.DARK) {
            sharePref.setTheme(ThemesTypes.LIGHT)
        } else {
            sharePref.setTheme(ThemesTypes.DARK)
        }
    }

    fun getBooleanDarkTheme(context: Context, isSystemTheme: Boolean): Boolean {
        val sharePref = SharedPrefsHelper(context)
        val theme = sharePref.getTheme(isSystemTheme)
        return theme == ThemesTypes.DARK
    }

    fun resetCheckIsOnline() {
        isInternetAvailable.value = isOnline(context)
    }

    fun updateListCategories() {
        if (selectedButton.value == ButtonState.DOHOD) {
            listForCategories.value = categoriesDohodList.value
        } else {
            listForCategories.value = categoriesRashodList.value
        }
    }

    suspend fun getAllDataFromSheet() {
        if (categoriesDohodList.value == emptyList<String>()) {
            resetCheckIsOnline()
            if (isOnline(context)) {
                showDialog()
                val responseDohod = try {
                    SheetApiClient.apiService.getCategories(
                        SharedPrefsHelper(context).getLink(),
                        "Категории",
                        3,
                        1,
                        33,
                        1
                    )
                } catch (e: IOException) {
                    delay(1000L)
                    _toastMessage.value = "Проверьте подключение к интернету"
                    null
                } catch (e: HttpException) {
                    _toastMessage.value = "Проверьте правильность ссылки: " + e.code()
                    null
                } catch (e: Exception) {
                    _toastMessage.value = "Проверьте правильность таблицы"
                    null
                }

                if (responseDohod != null) {
                    if (responseDohod.isSuccessful) {
                        val responseModel = responseDohod.body()
                        if (responseModel?.error != null) {
                            _toastMessage.value =
                                "Проверьте правильность ссылки: " + responseModel.error
                        } else {

                            if (responseModel != null) {

                                val list = mutableListOf<String>()
                                for (sublist in responseModel.data) {
                                    if (sublist[0].isNotEmpty()) {
                                        list.add(sublist[0])
                                    }
                                }
                                categoriesDohodList.value = list
                            }
                        }
                    } else {
                        _toastMessage.value =
                            "Проверьте правильность ссылки, error: " + responseDohod.code()
                    }
                } else {
                    _toastMessage.value = "Ошибка"
                }


                val responseRashod = try {
                    SheetApiClient.apiService.getCategories(
                        SharedPrefsHelper(context).getLink(),
                        "Категории",
                        3,
                        18,
                        33,
                        1
                    )
                } catch (e: IOException) {
                    delay(1000L)
                    _toastMessage.value = "Проверьте подключение к интернету"
                    null
                } catch (e: HttpException) {
                    _toastMessage.value = "Проверьте правильность ссылки: " + e.code()
                    null
                } catch (e: Exception) {
                    _toastMessage.value = "Проверьте правильность таблицы"
                    null
                }

                if (responseRashod != null) {
                    if (responseRashod.isSuccessful) {
                        val responseModel = responseRashod.body()
                        if (responseModel?.error != null) {
                            _toastMessage.value =
                                "Проверьте правильность ссылки: " + responseModel.error
                        } else {

                            if (responseModel != null) {

                                val list = mutableListOf<String>()
                                for (sublist in responseModel.data) {
                                    if (sublist[0].isNotEmpty()) {
                                        list.add(sublist[0])
                                    }
                                }
                                categoriesRashodList.value = list
                            }

                            _toastMessage.value = "Данные обновлены"
                        }
                    } else {
                        _toastMessage.value =
                            "Проверьте правильность ссылки, error: " + responseRashod.code()
                    }
                } else {
                    _toastMessage.value = "Ошибка"
                }
                hideDialog()

                val responseMoney = try {
                    SheetApiClient.apiService.getMoney(
                        SharedPrefsHelper(context).getLink(),
                        MonthNameHelper().getCurrentMonthName(),
                        2,
                        13,
                        1,
                        1
                    )
                } catch (e: IOException) {
                    delay(1000L)
                    _toastMessage.value = "Проверьте подключение к интернету"
                    null
                } catch (e: HttpException) {
                    _toastMessage.value = "Проверьте правильность ссылки: " + e.code()
                    null
                } catch (e: Exception) {
                    _toastMessage.value = "Проверьте правильность таблицы"
                    null
                }

                if (responseMoney != null) {
                    if (responseMoney.isSuccessful) {
                        val responseModel = responseMoney.body()
                        if (responseModel?.error != null) {
                            _toastMessage.value =
                                "Проверьте правильность ссылки: " + responseModel.error
                        } else {

                            if (responseModel != null) {
                                moneyFromSheet.value = responseModel.data[0][0]
                            }
                        }
                    } else {
                        _toastMessage.value =
                            "Проверьте правильность ссылки, error: " + responseMoney.code()
                    }
                } else {
                    _toastMessage.value = "Ошибка"
                }

            } else {
                hideDialog()
                _toastMessage.value = "Проверьте подключение к интернету"
            }
            updateListCategories()
        }
    }

    suspend fun createWrite(){
        resetCheckIsOnline()
        if(isOnline(context)){
            showDialog()
            val postResponce = try{
                SheetApiClient.apiService.insertData(
                    SharedPrefsHelper(context).getLink(),
                    MonthNameHelper().getCurrentMonthName(),
                    selectedButton.value.name,
                    selectedCategory.value,
                    currentDate.value.format(DateTimeFormatter.ofPattern("d.MM.yyyy")),
                    currentMoney.value,
                    currentComment.value
                )
            } catch (e: IOException) {
                delay(1000L)
                _toastMessage.value = "Проверьте подключение к интернету"
                null
            } catch (e: HttpException) {
                _toastMessage.value = "Проверьте правильность ссылки: " + e.code()
                null
            } catch (e: Exception) {
                _toastMessage.value = "Проверьте правильность таблицы"
                null
            }

            if (postResponce != null) {
                if (postResponce.isSuccessful) {
                    val responseModel = postResponce.body()
                    if (responseModel?.status == "success") {
                        selectedCategory.value = ""
                        currentMoney.value = ""
                        currentComment.value = ""
                        _toastMessage.value = "Таблица успешно обновлена: " + responseModel.status
                    } else {
                        _toastMessage.value = "Ошибка: " + responseModel?.status
                    }
                } else {
                    _toastMessage.value = "Проверьте правильность ссылки, error: " + postResponce.code()
                }
            } else {
                _toastMessage.value = "Ошибка"
            }

            hideDialog()
        } else {
            hideDialog()
            _toastMessage.value = "Проверьте подключение к интернету"
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}