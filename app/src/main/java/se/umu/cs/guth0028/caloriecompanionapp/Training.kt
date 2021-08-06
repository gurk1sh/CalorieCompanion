package se.umu.cs.guth0028.caloriecompanionapp

import androidx.room.*
import java.util.*

@Entity
data class Training (@PrimaryKey val id: UUID = UUID.randomUUID(), var name: String = "", var caloriesBurned: Float = 0f, var length: Int = 0) {

}