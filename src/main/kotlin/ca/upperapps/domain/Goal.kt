package ca.upperapps.domain

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

@MongoEntity(collection = "goals")
data class Goal @BsonCreator constructor(
    @BsonId val id: ObjectId,
    @BsonProperty("goal") val goal: String,
    @BsonProperty("user") val user: User,
    @BsonProperty("description") val description: String? = null,
    @BsonProperty("criteria") val criteria: List<Criteria>? = null,
    @BsonProperty("options") val options: List<Option>? = null,
    @BsonProperty("judgementMatrix") val judgementMatrix: JudgementMatrix? = null,
    @BsonProperty("scenario") val scenario: List<Scenario>? = null
): PanacheMongoEntityBase() {
    init {
        validate(this) {
            validate(Goal::goal).isNotBlank().hasSize(1, 50)
            validate(Goal::description).hasSize(0, 140)
        }
    }
}