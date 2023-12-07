package ca.upperapps.domain

import io.quarkus.mongodb.panache.common.ProjectionFor
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

@ProjectionFor(Goal::class)
data class GoalCriteria @BsonCreator constructor(
    @BsonProperty("criteria") val criteria: List<Criteria>?
) : PanacheMongoEntityBase()
