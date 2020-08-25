package main

import client.loadReviewDiffs
import client.loadReviewsData

const val testBaseUrl = "https://review.openstack.org"

fun main() {
    println(loadReviewDiffs(testBaseUrl))
}