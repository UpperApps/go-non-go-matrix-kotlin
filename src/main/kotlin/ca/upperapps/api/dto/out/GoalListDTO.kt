package ca.upperapps.api.dto.out

data class GoalListDTO(
    val pageCount: Int,
    val totalRecords: Long,
    val goals: List<GoalDTO>?
)