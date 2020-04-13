package vine.watcher.queue

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import vine.watcher.SlackPoster
import vine.watcher.WineStatus
import vine.watcher.WinesRepository
import vine.watcher.vinmonopolet.VMPClient

class CommandProcessor(
        private val slackPoster: SlackPoster = SlackPoster(),
        private val winesRepository: WinesRepository = WinesRepository(),
        private val vmpClient: VMPClient = VMPClient()
) : RequestHandler<SQSEvent, Unit> {

    override fun handleRequest(input: SQSEvent?, context: Context?) {
        if (input == null) {
            println("No input. Ok? Bai.")
            return
        }

        input.records.forEach { message ->
            try {
                handleMessage(message)
            } catch(e: Exception) {
                slackPoster.postMessage("Oh no, noe gikk riv, ruskende galt: ```$e```")
            }
        }
    }

    private fun handleMessage(message: SQSEvent.SQSMessage) {
        val bodyparts = message.body.split(" ")
        when (val command = bodyparts[0]) {
            "add" -> {
                val articleId = bodyparts[1]
                handleAdd(articleId)
            }
            "remove" -> {
                val articleId = bodyparts[1]
                handleDelete(articleId)
            }
            "list" -> {
                handleList()
            }
            else -> {
                slackPoster.postMessage("Ukjent kommando: $command")
            }
        }
    }

    private fun handleList() {
        val allWines = winesRepository.allWines()
                .groupBy {
                    if (it.disabled == null) "active"
                    else "inactive"
                }

        slackPoster.postMessage("*Aktive*: \n```${allWines["active"]?.joinToString("\n") { it.prettyPrint() }}```\n\n" +
                "*Inaktive*: ```${allWines["inactive"]?.joinToString("\n") { it.prettyPrint() }}```"
        )
    }

    private fun handleDelete(articleId: String) {
        val wine = winesRepository.getWine(articleId)

        if (wine != null) {
            winesRepository.disableWine(articleId)
            slackPoster.postMessage("Slettet $articleId ${wine.name}")
        }
        else {
            slackPoster.postMessage("Fant ingen viner med artikkel ID $articleId!")
        }
    }

    private fun handleAdd(articleId: String) {
        val product = vmpClient.getWineInfo(articleId).productSearchResult.products.firstOrNull()

        if (product == null) {
            slackPoster.postMessage("Fant ingenting med produktnummer $articleId")
        } else {
            winesRepository.createWine(WineStatus(product.code, product.name, null, null))
            slackPoster.postMessage("La til _${product.name}_ til ${product.price.formattedValue}")
        }
    }

}
