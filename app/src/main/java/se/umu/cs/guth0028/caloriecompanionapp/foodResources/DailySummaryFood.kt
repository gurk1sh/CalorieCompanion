package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity
class DailySummaryFood(@PrimaryKey val id: UUID = UUID.randomUUID(), var foodName: String = "", var weight: Float = 0f, var category: String = "") {
}

/*(tableName = "DailySummaryFoods", foreignKeys = [ForeignKey(entity = Food::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("id"),
    onDelete = CASCADE)]
)*/