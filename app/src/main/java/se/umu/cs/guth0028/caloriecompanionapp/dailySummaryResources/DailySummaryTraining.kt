package se.umu.cs.guth0028.caloriecompanionapp.dailySummaryResources

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training
import java.util.*

@Entity
class DailySummaryTraining(@PrimaryKey val id: UUID = UUID.randomUUID(), var trainingName: String = "", var length: Int = 0, var caloriesBurned: Float = 0f) {
}

/*(tableName = "DailySummaryTraining", foreignKeys = [ForeignKey(entity = Training::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("id"),
    onDelete = CASCADE)]
)*/