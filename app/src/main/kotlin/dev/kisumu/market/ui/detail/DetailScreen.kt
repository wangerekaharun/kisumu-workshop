package dev.kisumu.market.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.kisumu.market.data.Catalogue
import dev.kisumu.market.ui.formatPrice
import dev.kisumu.market.ui.photoFor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: Long, onBack: () -> Unit) {
  val product = Catalogue.product(id)
  val similar =
    (product.tags.flatMap { Catalogue.byTag[it].orEmpty() } + Catalogue.byWord[product.name.lowercase().substringBefore(' ')].orEmpty())
      .filter { it.id != id }
      .distinctBy { it.id }
      .take(5)

  Scaffold(
    topBar = { TopAppBar(title = { Text(product.name) }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }
  ) { padding ->
    Column(
      modifier = Modifier.padding(padding).verticalScroll(rememberScrollState()).testTag("detail"),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Image(
        painter = painterResource(photoFor(product.category)),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(4f / 3f),
      )
      Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(formatPrice(product.priceKsh, product.unit), style = MaterialTheme.typography.headlineSmall)
        Text("${product.seller} at ${product.market} market · ★ ${product.rating}")
        Text(product.description, style = MaterialTheme.typography.bodyLarge)
        Text("Similar", style = MaterialTheme.typography.titleMedium)
        similar.forEach { Text("${it.name} · ${it.market} · ${formatPrice(it.priceKsh, it.unit)}") }
      }
    }
  }
}
