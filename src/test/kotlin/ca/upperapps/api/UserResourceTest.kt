package ca.upperapps.api

import ca.upperapps.domain.User
import ca.upperapps.domain.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.bson.types.ObjectId
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`

@QuarkusTest
class UserResourceTest {
    companion object {
        lateinit var jsonParser: ObjectMapper

        @InjectMock
        lateinit var userRepository: UserRepository

        @BeforeAll
        @JvmStatic
        fun setup() {
            jsonParser = ObjectMapper()
        }
    }

    // TODO Fix the tests or remove comment
    @Test
    fun getGoalById() {
//        val user = User()
//        user.id = ObjectId()
//        user.firstName = "Test"
//        user.lastName = "User"
//        user.username = "testuser"
//        user.email = "user@email.com"
//
//        `when`(userRepository.findById(user.id!!)).thenReturn(user)
//
//        RestAssured.given()
//            .contentType(ContentType.JSON)
//            .`when`().get("/users/${user.id}")
//            .then()
//            .statusCode(200)
//            .extract()
//            .body()
//            .`as`(User::class.java).apply {
//                MatcherAssert.assertThat(this.id, CoreMatchers.`is`(user.id))
//                MatcherAssert.assertThat(this.firstName, CoreMatchers.`is`(user.firstName))
//
//            }
    }
}