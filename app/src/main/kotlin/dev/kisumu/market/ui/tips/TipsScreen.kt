package dev.kisumu.market.ui.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.kisumu.market.tips.TipsProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(onBack: () -> Unit) {
  val context = LocalContext.current
  val tips = remember { TipsProvider.load(context).tips() }

  Scaffold(
    topBar = { TopAppBar(title = { Text("Market tips") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }
  ) { padding ->
    Column(
      modifier = Modifier.padding(padding).padding(16.dp).testTag("tips"),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      tips.forEach { Text("• $it", style = MaterialTheme.typography.bodyLarge) }
    }
  }
}
