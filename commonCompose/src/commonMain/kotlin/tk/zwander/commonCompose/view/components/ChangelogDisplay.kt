package tk.zwander.commonCompose.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tk.zwander.common.data.changelog.Changelog
import tk.zwander.commonCompose.util.parseHtml
import tk.zwander.samloaderkotlin.strings

@Composable
fun ChangelogDisplay(
    changelog: Changelog
) {
    Card(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        border = BorderStroke(1.dp, Color(255, 255, 255, 100)),
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {
            if (changelog.secPatch != null) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = strings.security(changelog.secPatch.toString()),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
            }

            if (changelog.relDate != null) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = strings.release(changelog.relDate.toString()),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
            }

            if (changelog.notes != null) {
                Text(changelog.notes!!.parseHtml())
            }
        }
    }
}