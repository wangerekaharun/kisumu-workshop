package dev.kisumu.market.ui.market

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.kisumu.market.R
import dev.kisumu.market.data.Catalogue
import dev.kisumu.market.data.Product
import dev.kisumu.market.ui.formatPrice
import dev.kisumu.market.ui.thumbnail
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(onOpen: (Long) -> Unit, onTips: () -> Unit) {
  val listState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  val products = Catalogue.products.sortedBy { it.priceKsh }
  val showBackToTop = listState.firstVisibleItemIndex > 4

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = { TextButton(onClick = onTips) { Text("Tips") } },
      )
    },
    floatingActionButton = {
      if (showBackToTop) {
        ExtendedFloatingActionButton(onClick = { scope.launch { listState.animateScrollToItem(0) } }) {
          Text("Back to top")
        }
      }
    },
  ) { padding ->
    LazyColumn(state = listState, contentPadding = padding, modifier = Modifier.testTag("market_list")) {
      items(products) { product ->
        ProductRow(product, onClick = { onOpen(product.id) })
        HorizontalDivider()
      }
    }
  }
}

@Composable
private fun ProductRow(product: Product, onClick: () -> Unit) {
  val context = LocalContext.current
  val photo = remember(product.id) { thumbnail(context, product.category, 160, 120) }
  Row(
    modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Image(
      bitmap = photo.asImageBitmap(),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier.size(72.dp).clip(RoundedCornerShape(12.dp)),
    )
    Column(modifier = Modifier.weight(1f)) {
      Text(product.name, style = MaterialTheme.typography.titleMedium)
      Text("${product.seller} · ${product.market}", style = MaterialTheme.typography.bodyMedium)
      Text(formatPrice(product.priceKsh, product.unit), style = MaterialTheme.typography.labelLarge)
    }
    Text("★ ${product.rating}", style = MaterialTheme.typography.labelMedium)
  }
}
