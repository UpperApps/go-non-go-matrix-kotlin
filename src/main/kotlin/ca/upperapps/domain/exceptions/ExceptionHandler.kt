package ca.upperapps.domain.exceptions

import org.valiktor.ConstraintViolationException
import javax.ws.rs.BadRequestException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class ExceptionHandler : ExceptionMapper<Exception> {
    override fun toResponse(exception: Exception?): Response {
        return when (exception) {
            is EntityNotFoundException -> Response.status(Response.Status.NOT_FOUND)
                .entity(ErrorResponseBody(exception.message, EntityNotFoundException::class.simpleName))
                .build()
            is BadRequestException -> Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponseBody(exception.message, BadRequestException::class.simpleName))
                .build()
            is ConstraintViolationException -> Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorHandlerUtils.getValidationMessage(exception))
                .build()
            is IllegalArgumentException -> Response.status(Response.Status.BAD_REQUEST)
                .entity(exception)
                .build()
            else -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorResponseBody("Something unexpected happened. Error: ${exception?.message}", exception!!::class.simpleName))
                .build()
        }
    }

    data class ErrorResponseBody(val message: String?, val type: String? = "")
}


