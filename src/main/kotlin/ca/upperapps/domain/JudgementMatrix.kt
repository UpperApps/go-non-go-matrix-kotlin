package ca.upperapps.domain

import org.bson.types.ObjectId

// TODO Finish implementation
data class JudgementMatrix(val id: ObjectId, val goalId: ObjectId, val criteriaPairs: Set<CriteriaPair>)
