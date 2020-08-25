package client

import khttp.get
import model.DiffRequest
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

fun loadReviewsData(baseUrl: String): String {

    val status = "merged"
    val limit = "5"
    val revisionOptions = "CURRENT_REVISION"
    val filesOptions = "CURRENT_FILES"

    val request = get("$baseUrl/changes/?q=$status&n=$limit&o=$revisionOptions&o=$filesOptions")
    println("Request URL: ${request.url}")
    return request.text.substringAfter(")]}'")
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

    val randomDiffRequest = diffRequestInfo.get(Random.nextInt(diffRequestInfo.size))
    val response = get("$baseUrl/changes/${randomDiffRequest.changeID}/revisions/${randomDiffRequest.revisionID}/files/${randomDiffRequest.fileID}/diff")
    println("Diff Request URL: ${response.url}")
    return response.text
}