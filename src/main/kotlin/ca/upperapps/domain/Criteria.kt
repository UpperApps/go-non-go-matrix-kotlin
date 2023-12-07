package ca.upperapps.domain

import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate


data class Criteria @BsonCreator constructor(
    @BsonId val id: ObjectId,
    @BsonProperty("definition") val definition: String
) : PanacheMongoEntityBase() {
    init {
        validate(this) {
            validate(Criteria::definition).isNotBlank().hasSize(1, 300)
        }
    }
}
