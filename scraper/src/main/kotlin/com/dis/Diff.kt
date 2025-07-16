package com.dis

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.reactive.messaging.Incoming

@Path("/api/scrape")
class Diff {

    val client = HttpClient(CIO)
    {
        install(HttpTimeout)
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Incoming("websites")
    suspend fun scrape(website: String): String? {
        println("Scraping: $website")
        // replace & with %26
//        val targetUrl = "https://www.buy2play.ro/categorie-produs/jocuri/jocuri-playstation/jocuri-ps5-noi/?per_page=100" // 20 sec
//        val targetUrl = "https://altex.ro/jocuri-ps5/cpl/" // 20 sec
//        val targetUrl = "https://www.lumea-jocurilor.ro/ps5/jatekok" // 1 min
//        val targetUrl = "https://www.eneba.com/ro/store/psn-games" // this will have to be iterated into with /product
//        val targetUrl = "https://www.eneba.com/ro/psn-ea-sportstm-college-football-26-standard-edition-ps5-psn-key-united-states"
//        val targetUrl = "https://www.skroutz.ro/c/4306/jocuri-ps5.html" // prices are fkd
//        val targetUrl = "https://www.mobile-zone.ro/jocuri-ps5" // iterated
//        val targetUrl = "https://www.cel.ro/jocuri/platforma-i1090/playstation-5/" // looks good
        val targetUrl = website // looks good
        val token = "d0984ff1efff1a0824ae40bf4632ff25"

        val response: HttpResponse = client.get("https://api.diffbot.com/v3/list") {
            url {
                parameters.append("url", targetUrl)
                parameters.append("token", token)
            }
            headers {
                append(HttpHeaders.Accept, "application/json")
            }
            timeout {
                requestTimeoutMillis = 60000
            }
        }

        val statusCode = response.status
        val responseBodyAsText = response.bodyAsText()

        return responseBodyAsText
    }
}
