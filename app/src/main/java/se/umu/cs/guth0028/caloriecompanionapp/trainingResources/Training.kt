package se.umu.cs.guth0028.caloriecompanionapp.trainingResources

import androidx.room.*
import java.util.*

@Entity
data class Training (@PrimaryKey val id: UUID = UUID.randomUUID(), var name: String = "", var caloriesBurned: Float = 0f, var time: Int = 0) {

    /*
   * Represents a training object consising of name, calories burned and time
   * */

}