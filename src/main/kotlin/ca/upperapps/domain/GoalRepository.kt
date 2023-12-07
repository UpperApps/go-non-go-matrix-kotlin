package ca.upperapps.domain

import ca.upperapps.api.GoalResource
import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository
import io.quarkus.mongodb.panache.kotlin.PanacheQuery
import org.bson.types.ObjectId
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GoalRepository: PanacheMongoRepository<Goal> {
    fun listGoalCriteria(goalId: String, page: Int, size: Int): List<Criteria>? {
        return find("_id", ObjectId(goalId)).project(GoalCriteria::class.java).list().single().criteria
    }
}