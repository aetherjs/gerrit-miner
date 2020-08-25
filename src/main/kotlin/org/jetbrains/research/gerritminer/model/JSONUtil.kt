package org.jetbrains.research.gerritminer.model

import com.google.gson.GsonBuilder
import java.io.File

const val outputPath = "output/"

fun flushToFile(reviewsData: Collection<Review>, repoName: String) {
    val projectPath: String = System.getProperty("user.dir")
    val gson = GsonBuilder().setPrettyPrinting().create()
    val jsonString = gson.toJson(reviewsData)
    File(projectPath).resolve(outputPath + repoName).writeText(jsonString)
}