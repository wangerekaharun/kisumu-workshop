package dev.kisumu.market

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dev.kisumu.market.data.PriceTicker
import dev.kisumu.market.ui.theme.KisumuMarketTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    PriceTicker.addListener { change -> title = "Kisumu Market (${if (change >= 0) "+" else ""}$change)" }

    // Open a screen directly: adb shell am start -n dev.kisumu.market/.MainActivity --es screen tips
    val start =
      when (intent.getStringExtra("screen")) {
        "detail" -> listOf(Market, Detail(1))
        "tips" -> listOf(Market, Tips)
        else -> listOf(Market)
      }
    setContent {
      KisumuMarketTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { MainNavigation(start) }
      }
    }
  }
}
