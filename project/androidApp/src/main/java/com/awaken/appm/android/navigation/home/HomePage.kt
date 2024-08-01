package com.awaken.appm.android.navigation.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.awaken.appm.android.navigation.LoadDialog
import com.awaken.appm.android.navigation.viewmodels.HomeViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val paddingFields = 15.dp

@Composable
fun HomePage(navController: NavController, onThemeUpdated: () -> Unit) {

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val theme = isSystemInDarkTheme()
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel {
        HomeViewModel(context)
    }

    val openDialog = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val colorBorder = if(homeViewModel.getBooleanDarkTheme(context, theme)) Color.White else Color.Black

    val toastMessage by homeViewModel.toastMessage.collectAsState()

    if (homeViewModel.isDialogShown.collectAsState().value) {
        LoadDialog("Обновляем данные...")
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            homeViewModel.cleanToastMessage()
        }
    }

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE/*, Lifecycle.Event.ON_RESUME*/ -> {
                    homeViewModel.resetCheckIsOnline()
                    val exceptionHandler = CoroutineExceptionHandler{_ , throwable->
                        throwable.printStackTrace()
                    }
                    coroutineScope.launch(Dispatchers.Default + exceptionHandler) {
                        homeViewModel.getAllDataFromSheet()
                    }
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter)//.verticalScroll(rememberScrollState(), reverseScrolling = true
            ) {
                HomeTopPanel(navController, homeViewModel, onThemeUpdated)
                Text(
                    modifier = Modifier
                        .padding(0.dp, 25.dp, 0.dp, 25.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "НОВАЯ ЗАПИСЬ",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 0.dp, 10.dp, 60.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        onClick = {
                            homeViewModel.selectedButton.value = HomeViewModel.ButtonState.DOHOD
                                  homeViewModel.updateListCategories()
                            homeViewModel.selectedCategory.value = ""
                                  },
                        shape = RoundedCornerShape(10.dp),
                        border = if (homeViewModel.selectedButton.collectAsState().value == HomeViewModel.ButtonState.DOHOD) BorderStroke(5.dp, colorBorder) else BorderStroke(0.dp, Color.Transparent),
                        contentPadding = PaddingValues(15.dp)
                    ) {
                        Text(text = "ДОХОД", fontSize = 20.sp)
                    }
                    Button(
                        onClick = {homeViewModel.selectedButton.value = HomeViewModel.ButtonState.RASHOD
                            homeViewModel.updateListCategories()
                            homeViewModel.selectedCategory.value = ""
                                  },
                        shape = RoundedCornerShape(10.dp),
                        border = if (homeViewModel.selectedButton.collectAsState().value == HomeViewModel.ButtonState.RASHOD) BorderStroke(5.dp, colorBorder) else BorderStroke(0.dp, Color.Transparent),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6b26b)),
                                contentPadding = PaddingValues(15.dp)
                    ) {
                        Text(text = "РАСХОД", fontSize = 20.sp)
                    }
                    Button(onClick = {homeViewModel.selectedButton.value = HomeViewModel.ButtonState.KREDIT
                        homeViewModel.updateListCategories()
                        homeViewModel.selectedCategory.value = ""
                                     },
                        shape = RoundedCornerShape(10.dp),
                        border = if (homeViewModel.selectedButton.collectAsState().value == HomeViewModel.ButtonState.KREDIT) BorderStroke(5.dp, colorBorder) else BorderStroke(0.dp, Color.Transparent),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe06665)),
                                contentPadding = PaddingValues(15.dp)
                    ) {
                        Text(text = "КРЕДИТ", fontSize = 20.sp)
                    }
                }

                CategoriesDropdownMenu(homeViewModel)

                DateTextField(
                    value = homeViewModel.currentDate.collectAsState().value,
                    onValueChange = {homeViewModel.currentDate.value = it}
                )

                MoneyCountTextField(homeViewModel)
                CommentTextField(homeViewModel)

                val a = homeViewModel.selectedCategory.collectAsState().value.isNotEmpty()
                val c = homeViewModel.currentMoney.collectAsState().value.isNotEmpty()

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        if(a && c){
                            coroutineScope.launch(Dispatchers.Default) {
                                homeViewModel.createWrite()
                            }
                        }
                        else{
                            openDialog.value = true
                        }
                              },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(15.dp)
                ) {
                    Text(text = "ЗАПИСАТЬ", fontSize = 20.sp)
                }
            }
        }


    if(openDialog.value) {
        AlertDialog(
            title = { Text(text = "Заполнены не все поля") },
            text = {
                if(homeViewModel.selectedCategory.collectAsState().value.isEmpty() && homeViewModel.currentMoney.collectAsState().value.isEmpty()){
                    Text("Вы не указали категорию и сумму. Всё равно записать?")
                }
                else if(homeViewModel.selectedCategory.collectAsState().value.isEmpty()){
                    Text("Вы не указали категорию. Всё равно записать?")
                }
                else if(homeViewModel.currentMoney.collectAsState().value.isEmpty()){
                    Text("Вы не указали сумму. Всё равно записать?")
                }
                   },
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                Button(
                    {
                        coroutineScope.launch(Dispatchers.Default) {
                            homeViewModel.createWrite()
                        }
                        openDialog.value = false
                    },
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text("Отправить", fontSize = 15.sp)
                } },
            dismissButton = {
                Button(
                    {
                        openDialog.value = false
                    },
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text("Отмена", fontSize = 15.sp)
                }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDropdownMenu(homeViewModel: HomeViewModel) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingFields, 0.dp, paddingFields, 25.dp),
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(
           /* modifier = Modifier
                .padding(paddingFields, 0.dp, paddingFields, 0.dp)
                .fillMaxWidth(),*/
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                singleLine = true,
                value = homeViewModel.selectedCategory.collectAsState().value,
                onValueChange = {},
                placeholder = { Text(text = "КАТЕГОРИЯ", fontSize = 23.sp)},
                readOnly = true,
                shape = RoundedCornerShape(10.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 23.sp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .padding(paddingFields, 0.dp, paddingFields, 0.dp)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                homeViewModel.listForCategories.collectAsState().value.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item)
                               },
                        onClick = {
                            homeViewModel.selectedCategory.value = item
                            expanded = false
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTextField(
    value: LocalDate,
    onValueChange: (LocalDate) -> Unit
) {

    val open = remember { mutableStateOf(false)}

    if (open.value) {
        CalendarDialog(
            state = rememberUseCaseState(visible = true, true, onCloseRequest = { open.value = false } ),
            config = CalendarConfig(
                yearSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date(
                selectedDate = value
            ) { newDate ->
                onValueChange(newDate)
            },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingFields, 0.dp, paddingFields, 25.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(paddingFields, 0.dp, paddingFields, 0.dp)
                .fillMaxWidth()
                .clickable { //Click event
                    open.value = true
                },
            textStyle = LocalTextStyle.current.copy(fontSize = 23.sp),
            enabled = false,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Color.Transparent,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                )
            },
            value = value.format(DateTimeFormatter.ofPattern("d.MM.yyyy")),
            onValueChange = {},
        )
    }
}

@Composable
fun MoneyCountTextField(homeViewModel: HomeViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingFields, 0.dp, paddingFields, 25.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(paddingFields, 0.dp, paddingFields, 0.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 23.sp),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            placeholder = { Text(text = "СУММА", fontSize = 23.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            },
            value = homeViewModel.currentMoney.collectAsState().value,
            onValueChange = { homeViewModel.currentMoney.value = it.filter { it.isDigit() } },
        )
    }
}

@Composable
fun CommentTextField(homeViewModel: HomeViewModel){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingFields, 0.dp, paddingFields, 50.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(paddingFields, 0.dp, paddingFields, 0.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 23.sp),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            placeholder = { Text(text = "КОММЕНТАРИЙ", fontSize = 23.sp)},
            //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            },
            value = homeViewModel.currentComment.collectAsState().value,
            onValueChange = {homeViewModel.currentComment.value = it},
        )
    }
}