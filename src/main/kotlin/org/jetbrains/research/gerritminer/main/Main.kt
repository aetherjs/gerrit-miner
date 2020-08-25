package org.jetbrains.research.gerritminer.main

import org.jetbrains.research.gerritminer.client.Client
import org.jetbrains.research.gerritminer.model.flushToFile
import org.jetbrains.research.gerritminer.model.parseReviewsData
import java.lang.Integer.parseInt


fun main(args: Array<String>) {
    if (args.size == 3) {
        val baseUrl = args[0]
        val repoName = args[1]
        var limit = 0
        try {
            limit = parseInt(args[2])
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
        flushToFile(reviewsData, repoName)
    } else {
        println("Incorrect number of arguments! \n")
        printUsageInfo()
    }
}

fun printUsageInfo() {
    println("Usage: ./gradlew run --args='arg1 arg2 arg3' \nWhere arg1 - baseUrl (for instance: https://review.opendev.org), arg2 - project codename for saving results (i.e. opendev) and arg3 is the number of review to mine")
}