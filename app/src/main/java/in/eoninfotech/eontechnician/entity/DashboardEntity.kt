package `in`.eoninfotech.eontechnician.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class DashboardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val color: String?,
    val color21: String?,
    val drsColor: String?,
    val drsColor21: String?
)
