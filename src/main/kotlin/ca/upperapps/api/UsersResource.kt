package ca.upperapps.api

import ca.upperapps.api.dto.UserDTO
import ca.upperapps.api.dto.out.UserListDTO
import ca.upperapps.domain.Goal
import ca.upperapps.domain.User
import ca.upperapps.domain.UserRepository
import ca.upperapps.domain.exceptions.EntityNotFoundException
import ca.upperapps.domain.exceptions.ErrorHandlerUtils
import ca.upperapps.domain.exceptions.ExceptionHandler
import io.quarkus.mongodb.panache.kotlin.PanacheQuery
import io.quarkus.panache.common.Page
import org.bson.types.ObjectId
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.valiktor.ConstraintViolationException
import java.net.URI
import java.util.logging.Logger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/users")
@Tag(name = "User Resource", description = "Resource responsible for user operations.")
class UsersResource {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger: Logger = Logger.getLogger(javaClass.enclosingClass.name)
        private const val DEFAULT_PAGE_SIZE = 10
    }

    @Inject
    lateinit var userRepository: UserRepository

    @GET
    @Operation(
        summary = "Get users list",
        description = "Get a list with all users"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = SchemaType.OBJECT, implementation = UserListDTO::class)
                )]
            )
        ]
    )
    fun listAll(
        @Parameter(description = "Page Index (default = 0).") @QueryParam("page") page: Int?,
        @Parameter(description = "The size of the page to be returned.") @QueryParam("size") size: Int?
    ): Response {
        var users: PanacheQuery<User> = userRepository.findAll()
        users.page(Page.of(page ?: 0, size ?: DEFAULT_PAGE_SIZE))
        return Response.ok(users.list()
            .map { user -> UserDTO.fromDomain(user) }
            .let { UserListDTO(users.pageCount(), users.count(), it) })
            .build()
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Get user",
        description = "Get a user by id"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "User found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = SchemaType.OBJECT, implementation = UserDTO::class)
                )]
            ),
            APIResponse(
                responseCode = "404",
                description = "User not found for a given id",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            ),
            APIResponse(
                responseCode = "400",
                description = "Id format is not valid",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            )
        ]
    )
    @Throws(EntityNotFoundException::class)
    fun getUser(@PathParam("id") id: String): Response {
        return try {
            val user = userRepository.findById(ObjectId(id))

            if (user != null) Response.ok(UserDTO.fromDomain(user)).build()
            else throw EntityNotFoundException("User not found for id $id")

        } catch (e: IllegalArgumentException) {
            throw java.lang.IllegalArgumentException("Invalid id format $id")
        }
    }

    @POST
    @Operation(
        summary = "Create user",
        description = "Create a new user"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201",
                description = "User created successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = SchemaType.OBJECT, implementation = UserDTO::class)
                )]
            ),
            APIResponse(
                responseCode = "400",
                description = "ID format is not valid",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            )
        ]
    )
    fun createUser(userDTO: UserDTO): Response {
        val newUser = userDTO.toDomain()
        userRepository.persist(newUser)
        return Response.created(URI.create("/users/${newUser.id}")).entity(UserDTO.fromDomain(newUser)).build()
    }

    @PUT
    @Operation(
        summary = "Update user",
        description = "Updates user info"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201",
                description = "User updated successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = SchemaType.OBJECT, implementation = UserDTO::class)
                )]
            )
        ]
    )
    @Throws(ConstraintViolationException::class)
    fun updateUser(updatedUserDTO: UserDTO): Response {
        val updatedUser = updatedUserDTO.toDomain()
        userRepository.update(updatedUser)
        return Response.created(URI.create("/users/${updatedUser.id}")).entity(updatedUser).build()
    }

    @Operation(
        summary = "Delete user",
        description = "Delete a user by id"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "204",
                description = "User deleted successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = SchemaType.ARRAY, implementation = UserDTO::class)
                )]
            )
        ]
    )
    @DELETE
    @Path("/{id}")
    fun deleteUser(@PathParam("id") id: String): Response {
        userRepository.deleteById(ObjectId(id))
        return Response.noContent().build()
    }
}