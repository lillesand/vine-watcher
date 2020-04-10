package vine.watcher.vinmonopolet

data class VinmonopoletSearchResponse(
        val productSearchResult: ProductSearchResult
) {
    data class ProductSearchResult(
            val pagination: Pagination,
            val products: List<Product>
    ) {
        data class Pagination(
            val currentPage: Int,
            val pageSize: Int,
            val sort: String,
            val totalPages: Int,
            val totalResults: Int
        )
        data class Product(
                val availability: Availability,
                val availableForPickup: Boolean,
                val buyable: Boolean,
                val code: String,
                val images: List<Image>,
                val inStockSupplier: Boolean,
                val litrePrice: LitrePrice,
                val main_category: MainCategory,
                val main_country: MainCountry,
                val main_producer: MainProducer,
                val name: String,
                val price: Price,
                val product_selection: String,
                val status: String,
                val stock: Stock,
                val url: String,
                val volume: Volume,
                val volumePricesFlag: Boolean
        ) {
            data class Availability(
                    val deliveryAvailability: DeliveryAvailability,
                    val storeAvailability: StoreAvailability
            ) {
                data class DeliveryAvailability(
                    val available: Boolean,
                    val mainText: String
                )

                data class StoreAvailability(
                    val available: Boolean,
                    val mainText: String,
                    val stockLink: StockLink
                ) {
                    data class StockLink(
                        val link: Boolean,
                        val text: String
                    )
                }
            }

            data class Image(
                val format: String,
                val imageType: String,
                val url: String
            )

            data class LitrePrice(
                val currencyIso: String,
                val formattedValue: String,
                val priceType: String,
                val readableValue: String,
                val value: Double
            )

            data class MainCategory(
                val name: String?
            )

            data class MainCountry(
                val name: String?
            )

            data class MainProducer(
                val name: String?
            )

            data class Price(
                val currencyIso: String,
                val formattedValue: String,
                val priceType: String,
                val readableValue: String,
                val value: Double
            )

            data class Stock(
                val stockLevel: Int,
                val stockLevelStatus: String
            )

            data class Volume(
                val formattedValue: String,
                val value: Double
            )
        }

    }
}
