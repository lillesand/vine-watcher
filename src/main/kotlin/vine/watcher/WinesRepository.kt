package vine.watcher

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.*
import java.time.LocalDateTime

class WinesRepository {

    private var dynamodb: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()

    private val environment = System.getenv("STAGE") ?: "local"
    private val tableName = "vinewatcher-$environment"


    fun createWine(wineRequest: WineStatus) {
        dynamodb.putItem(tableName, mapOf(
                Pair("articleId", AttributeValue(wineRequest.articleId)),
                Pair("name", AttributeValue(wineRequest.name)),
                Pair("added", AttributeValue(LocalDateTime.now().toString()))
        ))
    }

    fun disableWine(articleId: String) {
        dynamodb.updateItem(tableName,
                mapOf(Pair("articleId", AttributeValue(articleId))),
                mapOf(Pair("disabled", AttributeValueUpdate(AttributeValue(LocalDateTime.now().toString()), AttributeAction.PUT))))
    }

    fun previousWineStatus(): List<WineStatus> {
        return dynamodb.scan(ScanRequest(tableName).withFilterExpression("attribute_not_exists(disabled)")).items
                .map { WineStatus.fromMap(it) }
    }


    fun allWines(): List<WineStatus> {
        return dynamodb.scan(tableName, listOf("articleId", "name", "status", "disabled")).items.map { WineStatus.fromMap(it) }
    }

    fun updateWineStatus(wineStatuses: List<WineStatus>) {
        wineStatuses.forEach {
            dynamodb.updateItem(tableName,
                    mapOf(Pair("articleId", AttributeValue(it.articleId))),
                    mapOf(Pair("status", AttributeValueUpdate(AttributeValue(it.status), AttributeAction.PUT))))
        }
    }

    fun getWine(articleId: String): WineStatus? {
        return try {
            val result = dynamodb.getItem(tableName, mapOf(Pair("articleId", AttributeValue(articleId))))
            WineStatus.fromMap(result.item)
        } catch (e: ResourceNotFoundException) {
            null
        }
    }

}

data class WineStatus(val articleId: String, val name: String, val status: String?, val disabled: String?) {
    fun prettyPrint(): String {
        return "$articleId: $name - $status"
    }

    companion object {
         fun fromMap(it: Map<String, AttributeValue>) = WineStatus(it["articleId"]?.s!!, it["name"]?.s!!, it["status"]?.s, it["disabled"]?.s)
     }
}

fun main() {
    val winesRepository = WinesRepository()
    println(winesRepository.previousWineStatus().joinToString("\n"))
}
