package org.jetbrains.research.gerritminer.client

import khttp.get
import khttp.responses.Response

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
                       revisionOptions: String = "CURRENT_REVISION",
                       commitOptions: String = "CURRENT_COMMIT",
                       filesOptions: String = "CURRENT_FILES",
                       accountsOptions: String = "DETAILED_ACCOUNTS",
                       labelsOptions: String = "DETAILED_LABELS"): String {
        return  "$baseUrl/changes/?q=$status&n=${limit}&o=$revisionOptions&o=$commitOptions&o=$filesOptions&o=$accountsOptions&o=$labelsOptions"
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
}

