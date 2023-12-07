package ca.upperapps.api.dto.out

import ca.upperapps.api.dto.CriteriaDTO
import ca.upperapps.api.dto.OptionDTO
import ca.upperapps.api.dto.UserDTO
import ca.upperapps.domain.Goal
import ca.upperapps.domain.JudgementMatrix
import ca.upperapps.domain.Scenario
import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GoalDTO(
    val id: ObjectId?,
    val goal: String,
    val user: UserDTO,
    val description: String? = null,
    val criteria: List<CriteriaDTO>? = null,
    val options: List<OptionDTO>? = null,
    val judgementMatrix: JudgementMatrix? = null,
    val scenario: List<Scenario>? = null
) {

    companion object {
        fun fromDomain(goal: Goal): GoalDTO {
            return GoalDTO(
                goal.id,
                goal.goal,
                UserDTO.fromDomain(goal.user),
                goal.description,
                goal.criteria?.map { criteria ->  CriteriaDTO.fromDomain(criteria) },
                goal.options?.map { option -> OptionDTO.fromDomain(option) },
                goal.judgementMatrix,
                goal.scenario
            )
        }
    }
}