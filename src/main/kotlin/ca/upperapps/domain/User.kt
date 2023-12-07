package ca.upperapps.domain

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.validate


@MongoEntity(collection = "users")
data class User @BsonCreator constructor(
    @BsonId val id: ObjectId,
    @BsonProperty("firstName") val firstName: String,
    @BsonProperty("lastName") val lastName: String,
    @BsonProperty("username") val username: String,
    @BsonProperty("email") val email: String,
    @BsonProperty("password") var password: String? = null
) : PanacheMongoEntityBase() {
    init {
        validate(this) {
            validate(User::firstName).isNotBlank().hasSize(1, 15)
            validate(User::lastName).isNotBlank().hasSize(1, 15)
            validate(User::email).isNotBlank().isEmail()
            validate(User::username).hasSize(5, 15)
        }
    }
}