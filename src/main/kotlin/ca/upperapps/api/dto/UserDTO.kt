package ca.upperapps.api.dto

import ca.upperapps.domain.User
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDTO (
    val id: ObjectId?,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String? = null
) {
    companion object {
        fun fromDomain(user: User): UserDTO {
            return UserDTO(
                user.id,
                user.firstName,
                user.lastName,
                user.username,
                user.email,
                user.password
            )
        }
    }

    fun toDomain(): User {
        return User(id ?: ObjectId(), firstName, lastName, username, email, password)
    }
}
