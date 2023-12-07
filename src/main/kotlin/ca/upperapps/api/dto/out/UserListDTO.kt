package ca.upperapps.api.dto.out

import ca.upperapps.api.dto.UserDTO

data class UserListDTO(
    val pageCount: Int,
    val totalRecords: Long,
    val users: List<UserDTO>
)