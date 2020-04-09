package vine.watcher

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.*

class WinesRepository {

    private var dynamodb: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()

    private val environment = System.getenv("STAGE") ?: "local"
    private val tableName = "vinewatcher-$environment"


    fun createWine(wineRequest: WineStatus) {
        dynamodb.putItem(tableName, mapOf(
                Pair("articleId", AttributeValue(wineRequest.articleId)),
                Pair("name", AttributeValue(wineRequest.name))
        ))
    }

    fun previousWineStatus(): List<WineStatus> {
        return dynamodb.scan(tableName, listOf("articleId", "name", "status")).items.map {
            WineStatus(it["articleId"]?.s!!, it["name"]?.s!!, it["status"]?.s)
        }
    }

    fun updateWineStatus(wineStatuses: List<WineStatus>) {
        wineStatuses.forEach {
            dynamodb.updateItem(tableName,
                    mapOf(Pair("articleId", AttributeValue(it.articleId))),
                    mapOf(Pair("status", AttributeValueUpdate(AttributeValue(it.status), AttributeAction.PUT))))
        }
    }

}

data class WineStatus(val articleId: String, val name: String, val status: String?)
