package org.jetbrains.research.gerritminer.main

import org.jetbrains.research.gerritminer.client.Client
import org.jetbrains.research.gerritminer.model.parseReviewsData
import java.lang.Integer.parseInt

const val testBaseUrl = "https://review.opendev.org"

fun main(args: Array<String>) {
    if (args.size == 2) {
        val baseUrl = args[0]
        var limit = 0
        try {
            limit = parseInt(args[1])
        } catch (e: NumberFormatException) {
            println("Second argument must be a number! \n")
            printUsageInfo()
            return
        }
        val client = Client()
        val query = client.constructQuery(baseUrl, "merged", limit)
        val reviewsDataJSON = client.getResponseText(query)
        val reviewsData = parseReviewsData(reviewsDataJSON)
        println(reviewsData)
    } else {
        println("Incorrect number of arguments! \n")
        printUsageInfo()
    }
}

fun printUsageInfo() {
    println("Usage: ./gradlew run --args='arg1 arg2' \nWhere arg1 - baseUrl (for instance: https://review.opendev.org) and arg2 - number of reviews to mine")
}