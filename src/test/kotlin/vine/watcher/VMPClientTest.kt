package vine.watcher

import vine.watcher.vinmonopolet.VMPClient
import kotlin.test.Test

class VMPClientTest {
    @Test fun get_wine_info() {
        val sut = VMPClient()
        val product = sut.getWineInfo("8304801")

        println(product.productSearchResult.products[0].name)
    }
}
