package com.awaken.appm.android.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awaken.appm.android.R

@Composable
fun ManualPage() {

    var aop by remember {
        mutableStateOf(false)
    }

    var webop by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ){
        Column {


            Text(
                fontSize = 21.sp,
                text = "Для работы приложения необходимо сделать таблицу общедоступной с доступом по ссылке, при этом все, у кого есть ссылка, должны иметь право на её редактирование. Это обязательное условие, позволяющее приложению работать с таблицей. Для того, чтобы это сделать, необходимо зайти в таблицу с браузера на компьютере и телефоне, открыть «Настройки доступа»\n\n"
            )

            Text(
                modifier = Modifier.clickable { aop = !aop },
                text = "Как открыть доступ в приложении\n",
                color = Color(0xFF006eff),
                fontSize = 21.sp,
                textDecoration = TextDecoration.Underline
            )

            if (aop) {
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.android1),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.android2),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.android3),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.android4),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.android5),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.android6),
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier.clickable { webop = !webop },
                text = "Как открыть доступ на ПК\n\n",
                color = Color(0xFF006eff),
                fontSize = 21.sp,
                textDecoration = TextDecoration.Underline
            )

            if (webop) {
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.web1),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.web2),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.web3),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.padding(5.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.web4),
                    contentDescription = null
                )
            }

            Text(
                fontSize = 21.sp,
                text = "\nИнформация на изображениях актуальна на лето 2024-го года. Если у вас возникли трудности, попросите помощи у знакомых или воспользуйтесь поисковиком.\n" +
                        "\n" +
                        "Доступ к вашей таблице смогут получить все, у кого есть ссылка, поэтому не распространяйте её. Данное приложение не перенаправляет вашу ссылку куда-либо и связывается только с сервером Google. В случае опасений за свою конфиденциальность не используйте приложение или изучите его код. Исходный код приложения выложен вместе с самим приложением, ссылка на странице с информацией в шаблонной таблице, опубликованной в Т-Ж.\n"
            )
            HyperlinkText(fullText = "Перейти к шаблонной таблице", linkText = listOf("Перейти к шаблонной таблице"), hyperlinks = listOf("https://docs.google.com/spreadsheets/d/1hnIc_hFZCzvPaPGMUksdEd-oI-U6XLdEpCSW3MZ1t5U/edit?gid=1684899095#gid=1684899095"), fontSize = 21.sp)
        }
    }
}