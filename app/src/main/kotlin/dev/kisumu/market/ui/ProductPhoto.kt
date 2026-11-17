package dev.kisumu.market.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import dev.kisumu.market.R
import java.text.NumberFormat

@DrawableRes
fun photoFor(category: String): Int =
  when (category) {
    "fish" -> R.drawable.photo_fish
    "vegetables" -> R.drawable.photo_vegetables
    "fruit" -> R.drawable.photo_fruit
    else -> R.drawable.photo_grain
  }

fun thumbnail(context: Context, category: String, width: Int, height: Int): Bitmap {
  val photo = BitmapFactory.decodeResource(context.resources, photoFor(category))
  return Bitmap.createScaledBitmap(photo, width, height, true)
}

fun formatPrice(priceKsh: Int, unit: String): String =
  "KSh ${NumberFormat.getIntegerInstance().format(priceKsh)} / $unit"
