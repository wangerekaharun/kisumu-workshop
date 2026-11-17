package dev.kisumu.market.tips

import android.content.Context

/** Supplies the Tips screen. The class to use is named in `assets/features.txt`. */
interface TipsProvider {
  fun tips(): List<String>

  companion object {
    fun load(context: Context): TipsProvider {
      val className = context.assets.open("features.txt").bufferedReader().use { it.readText().trim() }
      return Class.forName(className).getDeclaredConstructor().newInstance() as TipsProvider
    }
  }
}

class MarketTips : TipsProvider {
  override fun tips() =
    listOf(
      "Fish is cheapest at Dunga beach before 8 am, when the boats come in.",
      "Ask for the bulk price on omena: most sellers drop it from two tins up.",
      "Kibuye is busiest on Sunday; go on Tuesday for shorter queues.",
      "Mangoes from Homa Bay arrive on Thursday afternoons.",
      "Pay with M-Pesa and keep the message: it is your receipt.",
    )
}
