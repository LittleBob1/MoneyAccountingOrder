package com.awaken.appm.android.navigation

import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.awaken.appm.android.navigation.viewmodels.AddSheetViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddSheetPage(navController: NavController) {

    val addSheetViewModel : AddSheetViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    if (addSheetViewModel.isDialogShown.collectAsState().value) {
        LoadDialog("Сохраняем таблицу...")
    }

    val toastMessage by addSheetViewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            addSheetViewModel.cleanToastMessage()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,

    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                "Вставьте ссылку на Вашу google таблицу учёта доходов и расходов:",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 50.dp),
                fontSize = 20.sp
            )
            OutlinedTextField(
                value =addSheetViewModel.linkText.collectAsState().value.replace("\n", ""),
                onValueChange = { addSheetViewModel.linkText.value = it },
                placeholder = { Text("Ссылка") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp, 0.dp, 10.dp, 25.dp),
                maxLines = 3
            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    val exceptionHandler = CoroutineExceptionHandler{_ , throwable->
                        throwable.printStackTrace()
                    }

                    coroutineScope.launch(Dispatchers.Default + exceptionHandler) {
                        addSheetViewModel.trySaveLink(context, navController)
                    }
                          },
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    "Добавить",
                    textAlign = TextAlign.Center,
                )
            }
        }

        var f by remember { mutableStateOf(true)}

        //if(!keyboardAsState().value) {
            Text(
                text = "Как получить ссылку?",
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .align(Alignment.BottomCenter)
                    .clickable {
                        if (f) {
                            f = false
                            navController.navigate(Routes.Manual.route)
                        }
                    },
                color = Color(0xFF006eff),
            )
        //}
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val view = LocalView.current
    var isImeVisible by remember { mutableStateOf(false) }

    DisposableEffect(LocalWindowInfo.current) {
        val listener = ViewTreeObserver.OnPreDrawListener {
            isImeVisible = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) == true
            true
        }
        view.viewTreeObserver.addOnPreDrawListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }
    return rememberUpdatedState(isImeVisible)
}