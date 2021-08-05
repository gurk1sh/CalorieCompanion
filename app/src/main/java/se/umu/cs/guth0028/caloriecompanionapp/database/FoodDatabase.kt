package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import se.umu.cs.guth0028.caloriecompanionapp.Food

@Database(entities = [ Food::class ], version=2)
@TypeConverters(FoodTypeConverters::class)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodDao() : FoodDao

}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Food ADD COLUMN test TEXT NOT NULL DEFAULT ''"
        )
    }
}