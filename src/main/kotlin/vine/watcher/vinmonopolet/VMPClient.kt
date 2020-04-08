/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package vine.watcher.vinmonopolet

import com.beust.klaxon.Klaxon
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClients
import kotlin.system.measureTimeMillis


class VMPClient {

    private val klaxon = Klaxon()
    private val httpclient = HttpClients.createDefault()

    fun getStoreStatus(articleId: String): VinmonopoletAvailabilityResponse {
        val uri = URIBuilder("https://www.vinmonopolet.no/api/products/${articleId}/stock?pageSize=10&currentPage=0&fields=BASIC&latitude=59.9492182&longitude=10.7683369")

        val response = httpclient.execute(HttpGet(uri.build()))

        response.use {
            return klaxon.parse<VinmonopoletAvailabilityResponse>(it.entity.content)!!
        }
    }

    fun getWineInfo(articleId: String): VinmonopoletSearchResponse {
        val uri = URIBuilder("https://www.vinmonopolet.no/api/search?q=${articleId}&searchType=product&fields=FULL&pageSize=1").build()

        val response = httpclient.execute(HttpGet(uri))

        response.use {
            return klaxon.parse<VinmonopoletSearchResponse>(it.entity.content)!!
        }
    }


}

fun main(args: Array<String>) {
    val app = VMPClient()
    val availability = app.getStoreStatus("8278501")
    val time = measureTimeMillis {
        val wineInfo = app.getWineInfo("8278501")
        println(wineInfo)
    }
    println("Took $time ms")

    println(availability)
}