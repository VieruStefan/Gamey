package com.dis

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter

@Path("/hello")
class GreetingResource {
    @Inject
    @Channel("websites")
    lateinit var websiteRequestEmitter: Emitter<String>

    var websites: List<String> = listOf(
        "https://www.jocurinoi.ro/toate-jocurile%26filter_id=527%26limit=100",
        "https://www.buy2play.ro/categorie-produs/jocuri/jocuri-playstation/jocuri-ps5-noi/?per_page=100",
        "https://altex.ro/jocuri-ps5/cpl/",
        "https://www.lumea-jocurilor.ro/ps5/jatekok",
        "https://www.eneba.com/ro/store/psn-games",
//        "https://www.eneba.com/ro/psn-ea-sportstm-college-football-26-standard-edition-ps5-psn-key-united-states", product
        "https://www.skroutz.ro/c/4306/jocuri-ps5.html",
        "https://www.mobile-zone.ro/jocuri-ps5",
        "https://www.cel.ro/jocuri/platforma-i1090/playstation-5/"
    )

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun hello(): String? {
        for (website in websites) {
            websiteRequestEmitter.send(website)
        }
        return "hell"
    }

}