package com.dicoding.capstone.data.api


data class CataractResponse(
	val results: List<Result>
)

data class Result(
	val condition: String,
	val prediction_score: String
)