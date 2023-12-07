package ca.upperapps.domain.exceptions

import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.toMessage

class ErrorHandlerUtils {
    companion object {
        fun getValidationMessage(e: ConstraintViolationException) = e.constraintViolations
            .map { m -> "Property ${ m.property } is invalid: ${ m.toMessage().message }" }
    }
}