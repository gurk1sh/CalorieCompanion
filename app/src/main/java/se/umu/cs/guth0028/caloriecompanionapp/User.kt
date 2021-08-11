package se.umu.cs.guth0028.caloriecompanionapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(@PrimaryKey val id: UUID = UUID.randomUUID(), var name: String = "", var age: Int = 0, var gender: String = "", var weight: Float = 0f, var length: Float = 0f, var activityLevel: String = "",var accountCreated : Boolean = false) {
}