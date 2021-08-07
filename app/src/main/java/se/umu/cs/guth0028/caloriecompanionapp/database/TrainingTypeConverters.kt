package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import java.util.*

class TrainingTypeConverters { //Converts the UUID so it can be saved in database and back to it's original type

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}