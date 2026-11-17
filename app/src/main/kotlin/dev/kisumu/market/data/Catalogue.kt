package dev.kisumu.market.data

import android.content.Context
import org.json.JSONObject

data class Product(
  val id: Long,
  val name: String,
  val category: String,
  val seller: String,
  val market: String,
  val priceKsh: Int,
  val unit: String,
  val rating: Double,
  val tags: List<String>,
  val description: String,
)

/** Every product the markets sell, read from `assets/catalogue.json`. */
object Catalogue {
  lateinit var products: List<Product>
    private set

  lateinit var byTag: Map<String, List<Product>>
    private set

  /** Every word in a product's name, seller, market and description, for finding similar products. */
  lateinit var byWord: Map<String, List<Product>>
    private set

  fun load(context: Context) {
    val text = context.assets.open("catalogue.json").bufferedReader().use { it.readText() }
    val items = JSONObject(text).getJSONArray("items")
    products = List(items.length()) { i -> items.getJSONObject(i).toProduct() }
    byTag =
      products
        .flatMap { product -> product.tags.map { tag -> tag to product } }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, list) -> list.sortedByDescending { it.rating } }
    byWord =
      products
        .flatMap { product ->
          "${product.name} ${product.seller} ${product.market} ${product.description}"
            .lowercase()
            .split(Regex("[^a-z]+"))
            .filter { it.length > 2 }
            .distinct()
            .map { word -> word to product }
        }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, list) -> list.sortedWith(compareByDescending<Product> { it.rating }.thenBy { it.priceKsh }) }
  }

  fun product(id: Long): Product = products.first { it.id == id }

  private fun JSONObject.toProduct(): Product {
    val tags = getJSONArray("tags")
    return Product(
      id = getLong("id"),
      name = getString("name"),
      category = getString("category"),
      seller = getString("seller"),
      market = getString("market"),
      priceKsh = getInt("priceKsh"),
      unit = getString("unit"),
      rating = getDouble("rating"),
      tags = List(tags.length()) { tags.getString(it) },
      description = getString("description"),
    )
  }
}
