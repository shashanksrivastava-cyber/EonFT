package `in`.eoninfotech.eontechnician.db

import androidx.room.Database
import `in`.eoninfotech.eontechnician.dao.DashboardDao
import `in`.eoninfotech.eontechnician.entity.DashboardEntity


@Database(entities = [DashboardEntity::class], version = 1)
abstract class AppDatabase {
    abstract fun dashboardDao(): DashboardDao
}