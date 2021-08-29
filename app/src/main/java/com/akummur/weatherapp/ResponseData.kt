package com.akummur.weatherapp

data class ResponseData(
    val message: String,
    val user_id: Int,
    val profile_details: ProfileDetails
)

data class ProfileDetails(
    val is_profile_completed: Boolean,
    val rating: Double
)
