package `in`.eoninfotech.eontechnician.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import `in`.eoninfotech.eontechnician.entity.DashboardEntity

interface DashboardDao {

    @Query("SELECT * FROM dashboard LIMIT 1")
    fun getDashboard(): LiveData<DashboardEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDashboard(entity: DashboardEntity)

    @Query("DELETE FROM dashboard")
    suspend fun clearDashboard()
}