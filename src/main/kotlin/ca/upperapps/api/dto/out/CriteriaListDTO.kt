package ca.upperapps.api.dto.out

import ca.upperapps.api.dto.CriteriaDTO

data class CriteriaListDTO(
    val pageCount: Int,
    val totalRecords: Int,
    val criteria: List<CriteriaDTO>?
)
