package ca.upperapps.api.dto.`in`

import ca.upperapps.domain.Goal
import ca.upperapps.domain.User
import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GoalDTO(
    val goal: String,
    val description: String? = null
) {

    fun toDomain(user: User, goalId: String? = null): Goal {
        return Goal(
            id = if (goalId != null) ObjectId(goalId) else ObjectId(),
            goal = goal,
            user = user,
            description = description
        )
    }
}

