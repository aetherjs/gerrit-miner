package org.jetbrains.research.gerritminer.client

import khttp.get
import khttp.responses.Response
import org.jetbrains.research.gerritminer.model.Review
import org.jetbrains.research.gerritminer.model.parseReviewsData

/**
 * API burst limit
 */
const val REQUEST_TIMEOUT = 500L

class Client {

    private var timeSinceLastRequest = 0L
    private val lastRequestTime = 0L

    /**
     * Constructs a Gerrit API query from base url and optional parameters
     */
    fun constructQuery(baseUrl: String,
                       status: String = "merged",
                       limit: Int = 10,
                       offset: Int = 0,
                       revisionOptions: String = "CURRENT_REVISION",
                       commitOptions: String = "CURRENT_COMMIT",
                       filesOptions: String = "CURRENT_FILES",
                       accountsOptions: String = "DETAILED_ACCOUNTS",
                       labelsOptions: String = "DETAILED_LABELS"): String {
        return  "$baseUrl/changes/?q=$status&S=$offset&n=${limit}&o=$revisionOptions&o=$commitOptions&o=$filesOptions&o=$accountsOptions&o=$labelsOptions"
    }

    /**
     * Implemented rate-limit throttling to avoid exhausting gerrit API resources
     */
    private fun getResponse(query: String): Response {
        timeSinceLastRequest = System.currentTimeMillis() - lastRequestTime
        val waitFor = REQUEST_TIMEOUT - timeSinceLastRequest
        if (waitFor > 0) {
            println("Waiting for $waitFor milliseconds to avoid overload")
            Thread.sleep(waitFor)
        }
        return get(query)
    }

    /**
     * Returns a JSON string response from an API call
     */
    fun getResponseText(query: String): String {
        val response = getResponse(query)
        return response.text.substringAfter(")]}'")
    }

    fun loadGerritReviews(baseUrl: String, status: String, limit: Int): List<Review> {
        val reviewsData = mutableListOf<Review>()
        var offset = 0
        var limitNotReached = true
        while (limitNotReached) {
            val query = constructQuery(baseUrl, status, limit, offset)
            val reviewsDataJSON = getResponseText(query)
            reviewsData.addAll(parseReviewsData(reviewsDataJSON).first)
            offset += parseReviewsData(reviewsDataJSON).first.size
            limitNotReached = parseReviewsData(reviewsDataJSON).second && offset < limit
        }
        return reviewsData
    }

}

