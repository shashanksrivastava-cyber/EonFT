package `in`.eoninfotech.eontechnician.repository

import `in`.eoninfotech.eontechnician.webservice.NewApiholder

class YearRepository (private val api: NewApiholder){

    suspend fun fetchYears(): Result<List<String>> {
        return try {
            val response = api.getYearList()
            if (response.isSuccessful && response.body()?.type == 1) {
                val years = response.body()?.yearDetail
                    ?.map { it.year }
                    ?: emptyList()
                Result.success(years)
            } else {
                Result.failure(Exception("Invalid response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}