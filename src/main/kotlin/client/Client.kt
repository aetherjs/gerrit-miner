package client

import khttp.get
import khttp.responses.Response
import model.DiffRequest
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

const val REQUEST_TIMEOUT = 500

class Client {
    private var timeSinceLastRequest = 0L
    private val lastRequestTime = 0L

    private fun loadReviewsData(baseUrl: String): String {

        val status = "merged"
        val limit = "5"
        val revisionOptions = "CURRENT_REVISION"
        val filesOptions = "CURRENT_FILES"

        val response = getResponse("$baseUrl/changes/?q=$status&n=$limit&o=$revisionOptions&o=$filesOptions")
        println("Request URL: ${response.url}")
        return response.text.substringAfter(")]}'")
    }

    fun loadReviewDiffs(baseUrl: String): String {
        val diffRequestInfo = mutableListOf<DiffRequest>()

        val jsonObject = JSONArray(loadReviewsData(baseUrl))
        jsonObject.forEach{
            val changeID = (it as JSONObject).getInt("_number")
            val revisions = (it as JSONObject).getJSONObject("revisions")
            revisions.keys().forEach {
                val revisionID = it
                val files = revisions.getJSONObject(it).getJSONObject("files")
                files.keys().forEach {path ->
                    val fileID = it
                    diffRequestInfo.add(DiffRequest(changeID, revisionID, fileID))
                }
            }
        }
        println(diffRequestInfo)
        val randomDiffRequest = diffRequestInfo[Random.nextInt(diffRequestInfo.size)]
        val query = "$baseUrl/changes/${randomDiffRequest.changeID}/revisions/${randomDiffRequest.revisionID}/files/${randomDiffRequest.fileID}/diff"
        val response = getResponse(query)
        println("Diff Request URL: ${response.url}")
        return response.text.substringAfter(")]}'")
    }

    fun getResponse(query: String): Response {
        timeSinceLastRequest = System.currentTimeMillis() - lastRequestTime
        val waitFor = REQUEST_TIMEOUT - timeSinceLastRequest
        if (waitFor > 0) {
            println("Waiting for $waitFor milliseconds to avoid overload")
            Thread.sleep(waitFor)
        }
        return get(query)
    }

    fun getResponseText(query: String): String {
        val response = getResponse(query)
        return response.text.substringAfter(")]}'")
    }


}

