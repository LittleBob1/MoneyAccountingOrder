package com.awaken.appm.android.navigation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.awaken.appm.android.navigation.Routes
import com.awaken.appm.android.navigation.viewmodels.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeTopPanel(navController: NavController, homeViewModel: HomeViewModel, onThemeUpdated: () -> Unit){

    val theme = isSystemInDarkTheme()
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        shape = RoundedCornerShape(
            bottomEnd = 20.dp,
            bottomStart = 20.dp,
        )

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(homeViewModel.isInternetAvailable.collectAsState().value) {
                Text(
                    modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp),
                    text = "БАЛАНС: ${homeViewModel.moneyFromSheet.collectAsState().value}р",
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp
                )
            } else {
                Row(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "НЕТ ПОДКЛЮЧЕНИЯ",
                        modifier = Modifier
                            .padding(20.dp, 0.dp, 10.dp, 0.dp)
                            .clickable {
                                homeViewModel.resetCheckIsOnline()
                                coroutineScope.launch (Dispatchers.Default){
                                    homeViewModel.getAllDataFromSheet()
                                }
                            },
                    )
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.clickable {
                        homeViewModel.resetCheckIsOnline()
                        coroutineScope.launch (Dispatchers.Default){
                            homeViewModel.getAllDataFromSheet()
                        }
                    })
                }
            }

            Box {
                IconButton(
                    onClick = { expanded = true },
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            //expanded = false
                            homeViewModel.changeTheme(context, theme)
                            onThemeUpdated()
                        },
                        text = { Text("Сменить тему") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            navController.navigate(Routes.Information.route)
                            expanded = false
                        },
                        text = { Text("Информация") }
                    )
                    Divider()
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            openDialog.value = true
                        },
                        text = { Text("Изменить таблицу") }
                    )
                }
            }
        }
    }

    if(openDialog.value) {
        AlertDialog(
            title = { Text(text = "Подтверждение действия") },
            text = { Text("Вы действительно хотите заменить таблицу?") },
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                Button(
                    {
                        homeViewModel.resetLink(context, navController)
                        openDialog.value = false
                    },
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text("Заменить", fontSize = 15.sp)
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