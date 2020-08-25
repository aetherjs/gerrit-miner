package model

import java.util.*

data class DiffRequest(val changeID: Int, val revisionID: String, val fileID: String)

data class Approval(val userID: String, val approveValue: Int, val grantDate: Date)

data class Review(
    val status: String,
    val approveHistory: List<Approval>,
    val submitDate: Date,
    val changeId: String,
    val project: String,
    val closeDate: Date,
    val files: List<String>
)