package com.taskail.mixion.data.models
import com.fasterxml.jackson.annotation.JsonProperty


/**
 *Created by ed on 4/16/18.
 */


data class AccountVotes(
		@JsonProperty("authorperm") val authorperm: String,
		@JsonProperty("weight") val weight: Int,
		@JsonProperty("rshares") val rshares: Int,
		@JsonProperty("percent") val percent: Int,
		@JsonProperty("time") val time: String
)