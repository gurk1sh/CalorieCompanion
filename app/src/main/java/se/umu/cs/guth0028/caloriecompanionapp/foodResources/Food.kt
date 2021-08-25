package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Food (@PrimaryKey val id: UUID = UUID.randomUUID(), var name: String = "", var category: String = "", var protein: Float = 0f, var fat: Float = 0f, var carbohydrates: Float = 0f) :
    Parcelable {
    val photoFileName
            get() = "IMG_$id.jpg"

    /*
    * Represents a food per 100g weight
    * */

}