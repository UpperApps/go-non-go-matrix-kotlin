package ca.upperapps.api.dto

import ca.upperapps.domain.Criteria
import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CriteriaDTO(val id: ObjectId?, val definition: String) {
    companion object {
        fun fromDomain(criteria: Criteria): CriteriaDTO {
            return CriteriaDTO(
                criteria.id,
                criteria.definition
            )
        }
    }

    fun toDomain(): Criteria {
        return Criteria(
            id = id ?: ObjectId(),
            definition = definition
        )
    }
}
