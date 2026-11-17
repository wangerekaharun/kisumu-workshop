package dev.kisumu.market

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.kisumu.market.ui.detail.DetailScreen
import dev.kisumu.market.ui.market.MarketScreen
import dev.kisumu.market.ui.tips.TipsScreen

@Composable
fun MainNavigation(start: List<NavKey>) {
  val backStack = rememberNavBackStack(*start.toTypedArray())
  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Market> { MarketScreen(onOpen = { backStack.add(Detail(it)) }, onTips = { backStack.add(Tips) }) }
        entry<Detail> { key -> DetailScreen(key.id, onBack = { backStack.removeLastOrNull() }) }
        entry<Tips> { TipsScreen(onBack = { backStack.removeLastOrNull() }) }
      },
  )
}
