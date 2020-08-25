package main

import client.Client
import model.parseReviewsData

const val testBaseUrl = "https://review.opendev.org"

fun main() {
    val client = Client()
    val query = client.constructQuery(testBaseUrl, "merged", 5)
    val reviewsDataJSON = client.getResponseText(query)
    val reviewsData = parseReviewsData(reviewsDataJSON)
    println(reviewsData)
}