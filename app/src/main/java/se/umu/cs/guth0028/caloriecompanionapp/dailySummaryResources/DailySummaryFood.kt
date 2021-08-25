package se.umu.cs.guth0028.caloriecompanionapp.dailySummaryResources

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
class DailySummaryFood(@PrimaryKey val id: UUID = UUID.randomUUID(), var foodName: String = "", var weight: Float = 0f, var category: String = "", var foodCategory: String = "", var protein: Float = 0f, var fat: Float = 0f, var carbs: Float = 0f, var calories: Float = 0f) :
    Parcelable {
}

/*(tableName = "DailySummaryFoods", foreignKeys = [ForeignKey(entity = Food::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("id"),
    onDelete = CASCADE)]
)*/