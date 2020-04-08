package vine.watcher.queue

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SQSEvent
import vine.watcher.SlackPoster
import vine.watcher.vinmonopolet.VMPClient

class CommandProcessor(
        private val slackPoster: SlackPoster = SlackPoster()
) : RequestHandler<SQSEvent, Unit> {

    override fun handleRequest(input: SQSEvent?, context: Context?) {
        if (input == null) {
            println("No input. Ok? Bai.")
            return
        }

        input.records.forEach { message ->
            val command = message.body.split(" ")[0]
            if (command == "add") {
                val articleId = message.body.split((" "))[1]
                val product = VMPClient().getWineInfo(articleId).productSearchResult.products.firstOrNull()

                if (product == null) {
                    slackPoster.postMessage("Fant ingenting med produktnummer $articleId")
                } else {
                    slackPoster.postMessage("La til _${product.name}_ til ${product.price.formattedValue}")
                }

            } else {
                slackPoster.postMessage("Ukjent kommando: $command")
            }
        }
    }

}
