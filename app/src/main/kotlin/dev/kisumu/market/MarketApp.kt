package dev.kisumu.market

import android.app.Application
import dev.kisumu.market.data.Catalogue
import dev.kisumu.market.data.PriceTicker

class MarketApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Catalogue.load(this)
    PriceTicker.start()
  }
}
