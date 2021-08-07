package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import androidx.room.*
import java.util.*

@Entity
data class Food (@PrimaryKey val id: UUID = UUID.randomUUID(), var name: String = "", var category: String = "", var protein: Float = 0f, var fat: Float = 0f, var carbohydrates: Float = 0f) {
    val photoFileName
            get() = "IMG_$id.jpg"

}