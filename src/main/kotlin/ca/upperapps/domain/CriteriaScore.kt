package ca.upperapps.domain

import org.bson.types.ObjectId

// TODO Finish implementation
data class CriteriaScore(
    val id: ObjectId = ObjectId(),
    var score: Int = 0
)