package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food

@Database(entities = [ Food::class ], version=1, exportSchema = false) //Represents a food database
@TypeConverters(FoodTypeConverters::class)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodDao() : FoodDao //Associates the entity class (foodDao) with the database

}

/*val food_migration_1_2 = object : Migration(1, 2) { //Migrates the database to another version if database data should be saved and schema needs to be changed
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Food ADD COLUMN test TEXT NOT NULL DEFAULT ''"
        )
    }
}*/