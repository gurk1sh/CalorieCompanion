package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import java.util.*

class FoodTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}