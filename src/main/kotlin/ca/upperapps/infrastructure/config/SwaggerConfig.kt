package ca.upperapps.infrastructure.config

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.info.License
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.ws.rs.core.Application

@OpenAPIDefinition(
    tags = [Tag(name = "Go-Non-Go Matrix API", description = "API created as a backend for the go-non-go matrix application.")],
    info = Info(
        title = "Go-Non-Go Matrix API with Quarkus",
        version = "0.0.1",
        contact = Contact(
            name = "Rodrigo Melo",
            url = "http://upperapps.ca/contact-us",
            email = "rodrigo.melo@upperapps.ca"
        ),
        license = License(
            name = "MIT",
            url = "https://opensource.org/licenses/MIT" // TODO Change it for the final licence before make it public.
        )
    )
)
/**
 * Use the following url to open Swagger UI: http://localhost:8080/q/swagger-ui/
 */
class SwaggerConfig: Application()