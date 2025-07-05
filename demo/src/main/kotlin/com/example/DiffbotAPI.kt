package com.example

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

@Path("/api/diffbot")
class DiffbotAPI {

    val client = HttpClient(CIO)
    {
        install(HttpTimeout)
    }

    @GET
    @Path("/jocurinoi")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun jocurinoi(): String? {
        // replace & with %26
        val targetUrl = "https://www.jocurinoi.ro/toate-jocurile%26filter_id=527%26limit=100"
        val token = "d0984ff1efff1a0824ae40bf4632ff25"

        val response: HttpResponse = client.get("https://api.diffbot.com/v3/analyze") {
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