package tk.zwander.commonCompose.view.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soywiz.korio.async.launch
import tk.zwander.common.data.HistoryInfo
import tk.zwander.common.model.HistoryModel
import tk.zwander.common.util.ChangelogHandler
import tk.zwander.common.util.UrlHandler
import tk.zwander.common.util.getFirmwareHistoryString
import tk.zwander.commonCompose.util.vectorResource
import tk.zwander.commonCompose.view.components.HistoryItem
import tk.zwander.commonCompose.view.components.HybridButton
import tk.zwander.commonCompose.view.components.MRFLayout
import tk.zwander.commonCompose.view.components.StaggeredVerticalGrid
import tk.zwander.samloaderkotlin.strings

/**
 * Delegate HTML parsing to the platform until there's an MPP library.
 */
expect object PlatformHistoryView {
    suspend fun parseHistory(body: String): List<HistoryInfo>
}

private suspend fun onFetch(model: HistoryModel) {
    val historyString = getFirmwareHistoryString(model.model, model.region)

    if (historyString == null) {
        model.endJob(strings.historyError())
    } else {
        try {
            val parsed = PlatformHistoryView.parseHistory(
                historyString
            )

            model.changelogs = try {
                ChangelogHandler.getChangelogs(model.model, model.region)
            } catch (e: Exception) {
                println("Error retrieving changelogs")
                e.printStackTrace()
                null
            }
            model.historyItems = parsed
            model.endJob("")
        } catch (e: Exception) {
            e.printStackTrace()
            model.endJob(strings.historyErrorFormat(e.message.toString()))
        }
    }
}

/**
 * The History View.
 * @param model the History model.
 * @param onDownload a callback for when the user hits the "Download" button on an item.
 * @param onDecrypt a callback for when the user hits the "Decrypt" button on an item.
 */
@Composable
fun HistoryView(
    model: HistoryModel,
    onDownload: (model: String, region: String, fw: String) -> Unit,
    onDecrypt: (model: String, region: String, fw: String) -> Unit
) {
    val canCheckHistory = model.model.isNotBlank()
            && model.region.isNotBlank() && model.job == null

    val odinRomSource = buildAnnotatedString {
        pushStyle(
            SpanStyle(
                color = LocalContentColor.current,
                fontSize = 16.sp
            )
        )
        append(strings.source())
        append(" ")
        pushStyle(
            SpanStyle(
                color = MaterialTheme.colors.primary,
                textDecoration = TextDecoration.Underline
            )
        )
        pushStringAnnotation("OdinRomLink", "https://odinrom.com")
        append(strings.odinRom())
        pop()
    }

    Column(
        Modifier.fillMaxWidth()
    ) {
        val rowSize = remember { mutableStateOf(0.dp) }
        Row(
            modifier = Modifier.fillMaxWidth()
                .onSizeChanged { rowSize.value = it.width.dp }
        ) {
            HybridButton(
                onClick = {
                    model.historyItems = listOf()

                    model.job = model.scope.launch {
                        onFetch(model)
                    }
                },
                enabled = canCheckHistory,
                text = strings.checkHistory(),
                description = strings.checkHistory(),
                vectorIcon = vectorResource("refresh.xml"),
                parentSize = rowSize.value
            )

            if (model.job != null) {
                Spacer(Modifier.width(8.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                        .align(Alignment.CenterVertically),
                    strokeWidth = 2.dp
                )
            }

            Spacer(Modifier.weight(1f))

            HybridButton(
                onClick = {
                    model.endJob("")
                },
                enabled = model.job != null,
                text = strings.cancel(),
                description = strings.cancel(),
                vectorIcon = vectorResource("cancel.xml"),
                parentSize = rowSize.value
            )
        }

        Spacer(Modifier.height(8.dp))

        MRFLayout(model, model.job == null, model.job == null, false)

        ClickableText(
            text = odinRomSource,
            modifier = Modifier.padding(start = 4.dp),
            onClick = {
                odinRomSource.getStringAnnotations("OdinRomLink", it, it)
                    .firstOrNull()?.let { item ->
                        UrlHandler.launchUrl(item.item)
                    }
            }
        )

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (model.statusText.isNotBlank()) {
                Text(
                    text = model.statusText,
                )
            }

            AnimatedVisibility(
                visible = model.historyItems.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn {
                    item {
                        StaggeredVerticalGrid(
                            modifier = Modifier.fillMaxWidth(),
                            maxColumnWidth = 400.dp
                        ) {
                            (model.historyItems).forEachIndexed { index, historyInfo ->
                                HistoryItem(
                                    index,
                                    historyInfo,
                                    model.changelogs?.changelogs?.get(historyInfo.firmwareString.split("/")[0]),
                                    { onDownload(model.model, model.region, it) },
                                    { onDecrypt(model.model, model.region, it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
