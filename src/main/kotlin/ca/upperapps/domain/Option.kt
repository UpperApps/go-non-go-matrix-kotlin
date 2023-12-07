package ca.upperapps.domain

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.valiktor.functions.hasSize
import org.valiktor.validate

data class Option @BsonCreator constructor(
    @BsonId val id: ObjectId,
    @BsonProperty("name") val name: String,
    @BsonProperty("description") val description: String?,
    @BsonProperty("score") val score: Map<String, Int>?
) {
    init {
        validate(this) {
            validate(Option::name).hasSize(5, 30)
            validate(Option::description).hasSize(0, 140)
        }
    }
}