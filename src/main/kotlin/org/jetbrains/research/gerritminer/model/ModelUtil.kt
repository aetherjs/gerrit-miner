package org.jetbrains.research.gerritminer.model

import org.json.JSONArray
import org.json.JSONObject
import java.sql.Timestamp

/**
 * Reads JSON response received by org.jetbrains.research.gerritminer.client and parses it into a list of [Review] objects
 */
fun parseReviewsData(response: String): Pair<Collection<Review>, Boolean> {
    val reviewsData = mutableListOf<Review>()
    println(response)
    val jsonResponse = JSONArray(response)
    var moreChanges = false
    jsonResponse.forEach { (it as JSONObject)
        val timestamp = Timestamp.valueOf(it.getString("created"))
        val legacyId = it.getInt("_number")
        val stringId = it.getString("id")
        val owner = it.getJSONObject("owner")
        val author = readUserInfo(owner)
        val reviewersList = mutableListOf<UserInfo>()
        val reviewers = it.getJSONObject("reviewers")
        reviewers.keys().forEach { key ->
            if (reviewers.get(key) is JSONArray) {
                (reviewers.get(key) as JSONArray).forEach { reviewer ->
                    reviewer as JSONObject
                    val reviewerInfo = readUserInfo(reviewer)
                    if (reviewerInfo.email != "null" &&
                            !reviewerInfo.displayName.contains("CI") &&
                            !reviewerInfo.displayName.contains("Bot") &&
                            reviewerInfo != author) {
                        reviewersList.add(reviewerInfo)
                    }
                }
            }
        }
        val branch = it.getString("branch")
        val project = it.getString("project")
        var commitId = "n/a"
        val filePaths = mutableListOf<String>()
        val revisions = it.getJSONObject("revisions")
        revisions.keys().forEach { revisionId ->
            if (revisions.getJSONObject(revisionId).has("files")) {
                commitId = revisionId
                val files = revisions.getJSONObject(revisionId).getJSONObject("files")
                files.keys().forEach { filePath ->
                    filePaths.add(filePath)
                }
            }
        }
        val commitInfo = CommitInfo(commitId, project, branch)
        if (reviewersList.isNotEmpty()) {
            val review = Review(legacyId, stringId, filePaths, commitInfo, reviewersList, author, timestamp)
            reviewsData.add(review)
        }
        if (it.has("_more_changes")) {
            moreChanges = it.getBoolean("_more_changes")
        }
    }
    return Pair(reviewsData, moreChanges)
}

/**
 * Reads user data from a provided JSON object, with respect to optional parameters
 */
fun readUserInfo(user: JSONObject): UserInfo {
    var email = "null"
    if (user.has("email")) {
        email = user.getString("email")
    }
    var username = "null"
    if (user.has("username")) {
        username = user.getString("username")
    }
    var name = "null"
    if (user.has("name")) {
        name = user.getString("name")
    }
    return UserInfo(user.getInt("_account_id"), username, email, name)
}
