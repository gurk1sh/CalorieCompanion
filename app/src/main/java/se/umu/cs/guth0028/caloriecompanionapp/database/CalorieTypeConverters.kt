package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Category
import java.util.*

class CalorieTypeConverters { //Converts the UUID so it can be saved in database and back to it's original type

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}