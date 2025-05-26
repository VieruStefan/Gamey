package com.example

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import jakarta.json.Json
import jakarta.json.JsonObject
import jakarta.json.JsonObjectBuilder
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/api/scrape")
class ExampleResource {
    val limit = 100

    fun extractTitleAndPlatformRegex(gameName: String): Pair<String, String> {
        val regex = Regex("""\s*\(([^)]+)(?:\s*\))?""")
        val matchResult = regex.find(gameName)

        return if (matchResult != null) {
            val platform = matchResult.groupValues.getOrNull(1) ?: "no platform"
            val title = gameName.substring(0, matchResult.range.first).trim()
            Pair(title.ifEmpty { gameName }, platform)
        } else {
            Pair(gameName, "no platform")
        }
    }

    @GET
    @Path("/ozone")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun ozone(): JsonObject? {
        val doc: Document = Ksoup.parseGetRequest(url = "https://www.ozone.ro/gaming/jocuri-video/?limit=${limit}")
        val temp = doc.select("a.product-box")
        val result: JsonObjectBuilder = Json.createObjectBuilder()
        var index = 0

        for (i in temp) {
            index++
            val currentElement = Json.createObjectBuilder()
            val titleElements = i.select("span.title")
            val (title, platform) = extractTitleAndPlatformRegex(titleElements.text().trim())
            val priceJson = Json.createObjectBuilder()
            val priceElements = i.select("span.price")

            if (priceElements.size > 1) {
                // we have an old price and new price
                val oldPrice: String = priceElements[0].text().trim()
                val price: String = priceElements[1].text().trim()
                priceJson.add("price", price)
                priceJson.add("oldPrice", oldPrice)
            }
            else
            {
                val price: String = priceElements[0].text().trim()
                priceJson.add("price", price)
            }
            currentElement.add("title", title)
                .add("platform", platform)
                .add("price", priceJson.build())

            result.add("$index", currentElement.build())
        }
        return result.build()
    }

    @GET
    @Path("/jocurinoi")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun jocuriNoi(): JsonObject?
    {
        val doc: Document = Ksoup.parseGetRequest(url = "https://www.jocurinoi.ro/toate-jocurile&limit=${limit}")
        val temp = doc.select("div.product-layout")
        val result: JsonObjectBuilder = Json.createObjectBuilder()
        var index = 0

        for (i in temp) {
            index++
            val currentElement = Json.createObjectBuilder()
            val platform: String = i.select("div.product-platform").text().trim()
            val price = i.select("span.price").text().trim()
            val oldPrice = i.select("span.market_price").text().trim()
            val title: String = i.select("a[href]").text().trim()
            val priceJson = Json.createObjectBuilder()

            priceJson.add("price", price)
            if (oldPrice.isNotEmpty())
            {
                priceJson.add("oldPrice", oldPrice)
            }

            currentElement.add("title", title)
                .add("platform", platform)
                .add("price", priceJson.build())

            result.add("$index", currentElement.build())
        }
        return result.build()
    }
}