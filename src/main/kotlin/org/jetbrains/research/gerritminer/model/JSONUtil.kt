package org.jetbrains.research.gerritminer.model

import com.google.gson.GsonBuilder
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

const val outputPath = "output/"

fun flushToFile(reviewsData: Collection<Review>, repoName: String) {
    val projectPath: String = System.getProperty("user.dir")
    val timeStamp: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val gson = GsonBuilder().setPrettyPrinting().create()
    val jsonString = gson.toJson(reviewsData)
    File(projectPath).resolve(outputPath + repoName + timeStamp).writeText(jsonString)
}