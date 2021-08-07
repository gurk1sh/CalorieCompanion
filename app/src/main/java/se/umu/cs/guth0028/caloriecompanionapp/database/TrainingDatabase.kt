package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training

@Database(entities = [ Training::class ], version=1, exportSchema = false) //Represents a training database
@TypeConverters(TrainingTypeConverters::class)
abstract class TrainingDatabase : RoomDatabase() {

    abstract fun trainingDao() : TrainingDao //Associates the entity class (trainingDao) with the database

}

/*val training_migration_1_2 = object : Migration(1, 2) { //Migrates the database to another version if database data should be saved and schema needs to be changed
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Training ADD COLUMN test TEXT NOT NULL DEFAULT ''"
        )
    }
}*/