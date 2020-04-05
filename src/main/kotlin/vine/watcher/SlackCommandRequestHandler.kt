package vine.watcher

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.github.seratch.jslack.app_backend.vendor.aws.lambda.request.ApiGatewayRequest
import com.github.seratch.jslack.app_backend.vendor.aws.lambda.response.ApiGatewayResponse
import com.github.seratch.jslack.app_backend.vendor.aws.lambda.util.SlackSignatureVerifier
import vine.watcher.vinmonopolet.VMPClient

class SlackCommandRequestHandler: RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    private val signatureVerifier = SlackSignatureVerifier()

    override fun handleRequest(request: ApiGatewayRequest?, context: Context?): ApiGatewayResponse {
        if (!signatureVerifier.isValid(request)) {
            return ApiGatewayResponse.builder().statusCode(401).build()
        }

        val articleId = request?.body?.split(" ")?.get(0)
        println(request?.body)

        //handle(articleId)

        return ApiGatewayResponse.builder()
                .rawBody("Fant ut artikkel-id $articleId")
                .build()
    }

    private fun handle(articleId: String?): ApiGatewayResponse? {
        if (articleId != null) {
            val product = VMPClient().getWineInfo(articleId).productSearchResult.products.firstOrNull()
            return if (product != null) {
                ApiGatewayResponse.builder()
                        .rawBody("${product.name} ${product.price.formattedValue}")
                        .build()
            } else {
                ApiGatewayResponse.builder()
                        .rawBody("Fant ingen viner med artikkel-id $articleId :(")
                        .build()
            }
        }
        return null
    }

}
