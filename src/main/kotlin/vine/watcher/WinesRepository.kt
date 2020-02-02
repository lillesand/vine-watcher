package vine.watcher

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.beust.klaxon.Klaxon

class WinesRepository {

    private val bucketName = "vinewatcher-prod"
    private val winesFileName = "wines.json"

    private val klaxon = Klaxon()
    private val client: AmazonS3

    init {
        val builder = AmazonS3ClientBuilder.standard()
        builder.region = "us-east-1"
        this.client = builder.build()
    }

    fun previousWineStatus(): List<WineStatus> {
        try {
            val json = client.getObjectAsString(bucketName, winesFileName) ?: return emptyList()
            return klaxon.parseArray(json) ?: emptyList()
        } catch (e: AmazonS3Exception) {
            if (e.errorCode == "NoSuchKey") {
                return emptyList();
            }
            throw e;
        }
    }


    fun saveWineStatus(wineStatus: List<WineStatus>) {
        val jsonString = klaxon.toJsonString(wineStatus)
        client.putObject(bucketName, winesFileName, jsonString)
    }

}


fun main() {

    val repo = WinesRepository()
    println(repo.previousWineStatus())
    repo.saveWineStatus(listOf(WineStatus("test", "fest")))
    println(repo.previousWineStatus())
}
