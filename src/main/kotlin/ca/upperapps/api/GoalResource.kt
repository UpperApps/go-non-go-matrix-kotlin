package ca.upperapps.api

import ca.upperapps.api.dto.CriteriaDTO
import ca.upperapps.api.dto.out.CriteriaListDTO
import ca.upperapps.api.dto.out.GoalDTO
import ca.upperapps.api.dto.out.GoalListDTO
import ca.upperapps.domain.*
import ca.upperapps.domain.exceptions.EntityNotFoundException
import ca.upperapps.domain.exceptions.ExceptionHandler
import io.quarkus.panache.common.Page
import org.bson.types.ObjectId
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.valiktor.ConstraintViolationException
import java.net.URI
import java.util.logging.Logger
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/users/{userId}/goals")
@Tag(name = "Goal Resource", description = "Resource responsible for Goals operations.")
class GoalResource {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger: Logger = Logger.getLogger(javaClass.enclosingClass.name)
        private const val DEFAULT_PAGE_SIZE = 10
    }

    @Inject
    private lateinit var goalRepository: GoalRepository

    @Inject
    private lateinit var goalService: GoalService

    @Inject
    private lateinit var userRepository: UserRepository

    @GET
    @Operation(
        summary = "Get user's goals list",
        description = "Get a list with all goals available for a user"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "List of goals returned",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = SchemaType.OBJECT, implementation = GoalListDTO::class)
                )]
            )
        ]
    )
    fun listAll(
        @PathParam("userId") userId: String,
        @Parameter(description = "Page Index (default = 0).") @QueryParam("page") page: Int?,
        @Parameter(description = "The size of the page to be returned.") @QueryParam("size") size: Int?
    ): Response {
        val goals = goalRepository.find("user._id", ObjectId(userId))// goalRepository.listAllUserGoals(userId)
        goals.page(Page.of(page ?: 0, size ?: DEFAULT_PAGE_SIZE))
        return Response.ok(goals.list()
            .map { goal -> GoalDTO.fromDomain(goal) }
            .let { GoalListDTO(goals.pageCount(), goals.count(), it) })
            .build()
    }

    @GET
    @Path("/{goalId}")
    @Operation(
        summary = "Get goal by ID",
        description = "Get a user goal by goal ID."
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Goal found for given id",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = GoalDTO::class))]
            ),
            APIResponse(
                responseCode = "404",
                description = "Goal not found for a given id",
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
    fun getGoal(@PathParam("goalId") goalId: String): Response {
        return try {
            val goal = goalRepository.findById(ObjectId(goalId))

            if (goal != null) Response.ok(GoalDTO.fromDomain(goal)).build()
            else throw EntityNotFoundException("Goal not found for id $goalId")
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid id format $goalId")
        }
    }

    @POST
    @Operation(
        summary = "Create a new user's goal",
        description = "Create a new user's goal"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201",
                description = "Goal created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = GoalDTO::class))]
            ),
            APIResponse(
                responseCode = "404",
                description = "User not found when creating the goal",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            ),
            APIResponse(
                responseCode = "400",
                description = "Invalid data",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            )
        ]
    )
    @Throws(ConstraintViolationException::class)
    fun createGoal(@PathParam("userId") userId: String, goalDTO: ca.upperapps.api.dto.`in`.GoalDTO): Response? {
        val user = userRepository.findById(ObjectId(userId))

        if (user != null) {
            val goalSaved = goalService.save(goalDTO.toDomain(user))
            return Response.created(URI.create("/goals/${goalSaved.id}")).entity(GoalDTO.fromDomain(goalSaved)).build()
        } else {
            throw throw EntityNotFoundException("Goal not created. User not found for id $userId")
        }
    }

    @PUT
    @Path("/{goalId}")
    @Operation(
        summary = "Update a user's goal",
        description = "Update a user's goal"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201",
                description = "Goal updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = GoalDTO::class))]
            ),
            APIResponse(
                responseCode = "404",
                description = "User not found when updating the given goal",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            ),
            APIResponse(
                responseCode = "400",
                description = "Invalid data",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            )
        ]
    )
    @Throws(ConstraintViolationException::class)
    fun updateGoal(
        @PathParam("userId") userId: String,
        @PathParam("goalId") goalId: String,
        updatedGoalDTO: ca.upperapps.api.dto.`in`.GoalDTO
    ): Response {
        val user = userRepository.findById(ObjectId(userId))

        if (user != null) {
            val updatedGoal = goalService.updateGoalInfo(updatedGoalDTO.toDomain(user, goalId))
            return Response.created(URI.create("/goals/${updatedGoal.id}")).entity(GoalDTO.fromDomain(updatedGoal)).build()
        } else {
            throw throw EntityNotFoundException("Goal not updated. User not found for id $userId")
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Delete a user's goal",
        description = "Delete a user's goal"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "204",
                description = "User's goal deleted"
            )
        ]
    )
    fun deleteGoal(@PathParam("id") id: String): Response {
        goalRepository.deleteById(ObjectId(id))
        return Response.noContent().build()
    }

    @POST
    @Operation(
        summary = "Create a new goal criteria",
        description = "Create a new goal criteria"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201",
                description = "Criteria created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CriteriaDTO::class))]
            ),
            APIResponse(
                responseCode = "404",
                description = "User or goal not found when creating the criteria",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            ),
            APIResponse(
                responseCode = "400",
                description = "Invalid data",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            )
        ]
    )
    @Path("/{goalId}/criteria")
    @Throws(ConstraintViolationException::class)
    fun createCriteria(
        @PathParam("userId") userId: String,
        @PathParam("goalId") goalId: String,
        @RequestBody criteriaDTO: CriteriaDTO
    ): Response {
        val criteria: Criteria = goalService.saveCriteria(goalId, criteriaDTO.toDomain())
        return Response.created(URI.create("users/$userId/goals/$goalId/criteria/${criteria.id}"))
            .entity(CriteriaDTO.fromDomain(criteria)).build()
    }

    @GET
    @Operation(
        summary = "Get goal criteria list",
        description = "Get a list with all criteria of a given goal"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(type = SchemaType.OBJECT, implementation = CriteriaListDTO::class)
                )]
            )
        ]
    )
    @Path("/{goalId}/criteria")
    fun listAllCriteria(
        @PathParam("goalId") goalId: String,
        @Parameter(description = "Page Index (default = 0).") @QueryParam("page") page: Int?,
        @Parameter(description = "The size of the page to be returned.") @QueryParam("size") size: Int?
    ): Response {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val pageIndex = page ?: 0
        val criteriaList: List<Criteria>? = goalRepository.listGoalCriteria(goalId, pageIndex, pageSize)
        val totalRecords = criteriaList?.size ?: 0

        return Response.ok(
            CriteriaListDTO(
                pageCount(totalRecords, pageSize),
                totalRecords,
                getPagedList(list = criteriaList, page = pageIndex, size = pageSize)?.map { CriteriaDTO.fromDomain(it) })
        ).build()
    }

    @PUT
    @Operation(
        summary = "Update a goal criteria",
        description = "Update a goal criteria"
    )
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201",
                description = "Criteria updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CriteriaDTO::class))]
            ),
            APIResponse(
                responseCode = "404",
                description = "User or goal not found when updating the criteria",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            ),
            APIResponse(
                responseCode = "400",
                description = "Invalid data",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ExceptionHandler.ErrorResponseBody::class)
                )]
            )
        ]
    )
    @Path("/{goalId}/criteria/")
    @Throws(ConstraintViolationException::class)
    fun updateCriteria(
        @PathParam("userId") userId: String,
        @PathParam("goalId") goalId: String,
        @RequestBody criteriaDTO: CriteriaDTO
    ): Response {
        val criteria: Criteria = goalService.updateCriteria(goalId, criteriaDTO.toDomain())
        return Response.created(URI.create("users/$userId/goals/$goalId/criteria/${criteria.id}"))
            .entity(CriteriaDTO.fromDomain(criteria)).build()
    }

    @DELETE
    @Path("/{goalId}/criteria/{criteriaId}")
    fun deleteCriteria(@PathParam("goalId") goalId: String, @PathParam("criteriaId") criteriaId: String): Response {
        goalService.deleteCriteria(goalId, criteriaId)
        return Response.noContent().build()
    }

    @GET
    @Path("/{goalId}/options")
    fun listAllOptions(@PathParam("goalId") goalId: String): Response = Response.ok().build()

    // TODO Implement create Options
    @POST
    @Path("/{goalId}/options")
    fun saveOptions(option: List<Option>): Response {

        return Response.ok().build()
    }

    // TODO Implement update option
    @PUT
    @Path("/{goalId}/options")
    fun updateOptions(updatedOption: List<Option>): Response {
        return Response.ok().build()
    }

    // TODO Implement delete option
    @DELETE
    @Path("/{goalId}/options/{optionId}")
    fun deleteOption(@PathParam("goalId") goalId: String, @PathParam("optionId") optionId: String): Response {
        // TODO Implement this method

        return Response.noContent().build()
    }

    // TODO Consider move these methods to a helper class. They were created cause I wasn't able to use the Panache pagination for an
    //  embedded array of objects.
    private fun pageCount(totalRecords: Int, pageSize: Int): Int {
        return if (totalRecords == 0) 0
        else if (totalRecords % pageSize == 0) totalRecords / pageSize else (totalRecords / pageSize) + 1
    }

    private fun <T> getPagedList(list: List<T>?, page: Int, size: Int): List<T>? {
        return list?.chunked(size)?.get(page)
    }
}