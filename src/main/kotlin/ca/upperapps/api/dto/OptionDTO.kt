package ca.upperapps.api.dto

import ca.upperapps.domain.Criteria
import ca.upperapps.domain.Option
import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OptionDTO(
    val id: ObjectId?,
    val name: String,
    val description: String? = null,
    val score: Map<String, Int>? = null
) {

    companion object {
        fun fromDomain(option: Option): OptionDTO {
            return OptionDTO(
                option.id,
                option.name,
                option.description,
                option.score
            )
        }
    }

    fun toDomain(): Option {
        return Option(
            id = this.id ?: ObjectId(),
            name = this.name,
            description = this.description,
            score = this.score
        )
    }
}
