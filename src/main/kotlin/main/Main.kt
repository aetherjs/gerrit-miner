package main

import client.Client


const val testBaseUrl = "https://review.openstack.org"

fun main() {
    val client = Client()
    println(client.loadReviewDiffs(testBaseUrl))
}