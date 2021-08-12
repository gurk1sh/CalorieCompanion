package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.userResources.User
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Category
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training

@Database(entities = [ Food::class, Category::class, Training::class, User::class ], version=1, exportSchema = true) //Represents a food database
@TypeConverters(CalorieTypeConverters::class)
abstract class CalorieDatabase : RoomDatabase() {//Associate entity classes with the database

    abstract fun calorieDao() : CalorieDao
    abstract fun trainingDao() : TrainingDao
    abstract fun userDao() : UserDao

}

/*val food_migration_1_2 = object : Migration(1, 2) { //Migrates the database to another version if database data should be saved and schema needs to be changed
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Food ADD COLUMN test TEXT NOT NULL DEFAULT ''"
        )
    }
}*/