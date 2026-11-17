package dev.kisumu.market.data

import android.os.Handler
import android.os.Looper
import kotlin.random.Random

/** Pretends to stream the day's average price change, in shillings, every few seconds. */
object PriceTicker {
  private val listeners = mutableListOf<(Int) -> Unit>()
  private val handler = Handler(Looper.getMainLooper())
  private val tick =
    object : Runnable {
      override fun run() {
        val change = Random.nextInt(-15, 16)
        listeners.forEach { it(change) }
        handler.postDelayed(this, 5_000)
      }
    }

  fun start() {
    handler.removeCallbacks(tick)
    handler.postDelayed(tick, 5_000)
  }

  fun addListener(listener: (Int) -> Unit) {
    listeners += listener
  }

  fun removeListener(listener: (Int) -> Unit) {
    listeners -= listener
  }
}
