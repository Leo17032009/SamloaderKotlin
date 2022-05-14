package tk.zwander.commonCompose.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tk.zwander.common.model.BaseModel
import tk.zwander.samloaderkotlin.strings
import kotlin.math.roundToInt

/**
 * A progress indicator.
 * @param model the view model to use as a data source.
 */
@Composable
fun ProgressInfo(model: BaseModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val hasProgress = model.progress.first > 0 && model.progress.second > 0

        AnimatedVisibility(
            visible = hasProgress
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = (model.progress.first.toFloat() / model.progress.second),
                        modifier = Modifier.height(8.dp).weight(1f).align(Alignment.CenterVertically)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Column(
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    val currentMB = (model.progress.first.toFloat() / 1024.0 / 1024.0 * 100.0).roundToInt() / 100.0
                    val totalMB = (model.progress.second.toFloat() / 1024.0 / 1024.0 * 100.0).roundToInt() / 100.0

                    Text(
                        text = strings.mib(currentMB, totalMB),
                    )

                    Spacer(Modifier.height(8.dp))

                    val speedKBps = model.speed / 1024.0
                    val shouldUseMB = speedKBps >= 1 * 1024
                    val finalSpeed =
                        "${((if (shouldUseMB) (speedKBps / 1024.0) else speedKBps) * 100.0).roundToInt() / 100.0}"

                    Text(
                        text = "$finalSpeed ${if (shouldUseMB) strings.mibs() else strings.kibs()}",
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = strings.percent(
                            try {
                                (model.progress.first.toFloat() / model.progress.second * 100 * 100.0).roundToInt() / 100.0
                            } catch (e: IllegalArgumentException) {
                                0
                            }
                        ),
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }

        Text(
            text = model.statusText
        )
    }
}
