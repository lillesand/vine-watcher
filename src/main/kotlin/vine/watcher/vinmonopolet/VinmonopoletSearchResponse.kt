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
                val code: String,
                val name: String,
                val price: Price,
                val status: String,
                val volume: Volume
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
            data class Price(
                val formattedValue: String,
                val readableValue: String
            )

            data class Volume(
                val formattedValue: String,
                val value: Double
            )
        }

    }
}
