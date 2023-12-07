package ca.upperapps.domain

import ca.upperapps.domain.exceptions.EntityNotFoundException
import org.bson.types.ObjectId
import java.util.logging.Level
import java.util.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class GoalService {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger: Logger = Logger.getLogger(javaClass.enclosingClass.name)
    }

    @Inject
    private lateinit var goalRepository: GoalRepository

    fun save(goal: Goal): Goal {
        try {
            val goalToPersist = goal.copy(criteria = null, options = null, judgementMatrix = null, scenario = null)
            goalRepository.persist(goalToPersist)
            return goalRepository.findById(goalToPersist.id)!!
        } catch (e: NullPointerException) {
            val errorMessage = "Goal persisted not found: ${e.message}"
            logger.log(Level.SEVERE, errorMessage)
            throw EntityNotFoundException(errorMessage)
        } catch (e: Exception) {
            val errorMessage = "The goal couldn't be persisted: ${e.message}"
            logger.log(Level.SEVERE, errorMessage)
            throw Exception(errorMessage)
        }
    }

    fun updateGoalInfo(goalToUpdate: Goal): Goal {
        try {
            val goalToPersist = goalToUpdate.copy(goal = goalToUpdate.goal, description = goalToUpdate.description)
            goalRepository.update(goalToPersist)
            return goalRepository.findById(goalToPersist.id)!!
        } catch (e: NullPointerException) {
            val errorMessage = "Error on updating goal with id ${goalToUpdate.id}"
            logger.log(Level.SEVERE, errorMessage)
            throw EntityNotFoundException(errorMessage)
        }
    }

    // TODO It's necessary to recreate the matrix everytime we include or remove a new criteria.
    fun saveCriteria(goalId: String, criteria: Criteria): Criteria {
        return try {
            val goal = goalRepository.findById(ObjectId(goalId))!!
            val goalCriteria = goal.criteria

            val criteriaToPersist = goalCriteria?.plus(criteria) ?: listOf(criteria)

            goalRepository.update(goal.copy(criteria = criteriaToPersist))
            goalRepository.findById(ObjectId(goalId))!!.criteria!!.find { it.id == criteria.id }!!

        } catch (e: NullPointerException) {
            val errorMessage = "Goal not found: ${e.message}"
            logger.log(Level.SEVERE, errorMessage)
            throw EntityNotFoundException(errorMessage)
        }
    }

    fun updateCriteria(goalId: String, updatedCriteria: Criteria): Criteria {
        return try {
            val goal: Goal = goalRepository.findById(ObjectId(goalId))!!
            val criteriaList = goal.criteria

            val originalCriteria = criteriaList?.find { it.id == updatedCriteria.id }!!
            val index = criteriaList.indexOf(originalCriteria)
            var updatedCriteriaList = goal.criteria.toMutableList()
            index.let { updatedCriteriaList.set(it, updatedCriteria) }
            goalRepository.update(goal.copy(criteria = updatedCriteriaList))

            goalRepository.findById(ObjectId(goalId))?.criteria?.find { it.id == updatedCriteria.id }!!
        } catch (e: NullPointerException) {
            val errorMessage = "Goal or criteria not found: ${updatedCriteria.id}"
            logger.log(Level.SEVERE, "$errorMessage: $e")
            throw EntityNotFoundException(errorMessage)
        }
    }

    fun deleteCriteria(goalId: String, criteriaId: String) {
        try {
            val goal: Goal = goalRepository.findById(ObjectId(goalId))!!
            val criteriaList = goal.criteria

            val originalCriteria = criteriaList?.find { it.id == ObjectId(criteriaId) }!!
            var updatedCriteriaList = goal.criteria.toMutableList()
            updatedCriteriaList.remove(originalCriteria)
            goalRepository.update(goal.copy(criteria = updatedCriteriaList))

        } catch (e: NullPointerException) {
            val errorMessage = "Goal or criteria not found: $criteriaId"
            logger.log(Level.SEVERE, "$errorMessage: $e")
            throw EntityNotFoundException(errorMessage)
        }
    }
}