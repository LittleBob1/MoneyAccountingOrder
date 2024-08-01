package com.awaken.appm.android.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InformationPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ){
        HyperlinkText(
            fullText = "Я рад, что Вы пользуетесь моей таблицей, а теперь ещё и приложением! Я долго к этому шёл и действительно рад, что это увлечение развилось до такой степени и помогает не только мне, но и кому-то ещё :)\n" +
                "\n" +
                "Приложение имеет минимальный функционал. Для его работы необходимо, чтобы в таблице на листе «Категории» в ячейке М1 был указан текущий год, например «2024», категории доходов должны располагаться в ячейках A3:A35, а расходов в R3:R35. Приложение после подключения и обновления данных записывает указанные Вами данные в следующую свободную строчку указанной области (Доход/Расход/Кредит) на листе текущего месяца. При завершении года необходимо создать новую таблицу, указать в ней текущий год и подключить приложение к новой таблице по новой ссылке.\n" +
                "\n" +
                "Приложение имеет открытый исходный код и предоставляется «Как есть». Обновлений и поддержки не предусмотрено, но тем не менее свои предложения и вопросы можете задавать мне по e-mail, указанному в шаблонной таблице, опубликованной в Т-Ж.\n" +
                "\n" +
                "Приложение стоило мне денег, и если Вы желаете поддержать меня донатом, то реквизиты так же указаны в той самой шаблонной таблице х)\n" +
                "\n" +
                "Ссылка на статью\n\n" +
                "Ссылка на шаблонную таблицу от 2024 года\n" +
                "\n" +
                "Желаю успехов в ведении учёта, роста доходов и умеренных расходов!\n",
            linkText = listOf("Ссылка на статью", "Ссылка на шаблонную таблицу от 2024 года"),
            hyperlinks = listOf("https://journal.tinkoff.ru/spreadsheet-personal-finance/", "https://docs.google.com/spreadsheets/d/1hnIc_hFZCzvPaPGMUksdEd-oI-U6XLdEpCSW3MZ1t5U/edit?gid=1684899095#gid=1684899095"),
            fontSize = 21.sp)
    }
}