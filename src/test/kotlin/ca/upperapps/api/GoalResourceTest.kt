package ca.upperapps.api

import ca.upperapps.domain.Goal
import ca.upperapps.domain.GoalRepository
import ca.upperapps.domain.User
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bson.types.ObjectId
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`


@QuarkusTest
class GoalResourceTest {
    companion object {
        lateinit var jsonParser: ObjectMapper

        @InjectMock
        lateinit var goalRepository: GoalRepository

        @BeforeAll
        @JvmStatic
        fun setup() {
            jsonParser = ObjectMapper()
        }
    }

    // TODO Fix the tests or remove comments
    @Test
    fun getGoalById() {
//        val user = User()
//        user.id = ObjectId()
//        user.firstName = "Test"
//        user.lastName = "User"
//        user.username = "testuser"
//        user.email = "user@email.com"
//
//        val goal = Goal()
//        goal.id = ObjectId()
//        goal.name = "Test"
//        goal.user = user
//
//        `when`(goalRepository.findById(goal.id!!)).thenReturn(goal)
//
//        given()
//            .contentType(ContentType.JSON)
//            .`when`().get("/goals/${goal.id}")
//            .then()
//            .statusCode(200)
//            .extract()
//            .body()
//            .`as`(Goal::class.java).apply {
//                assertThat(this.id, `is`(goal.id))
//                assertThat(this.name, `is`(goal.name))
//                assertThat(this.user.firstName, `is`(goal.user.firstName))
//
//            }
    }
}