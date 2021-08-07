package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training

@Database(entities = [ Training::class ], version=2, exportSchema = false)
@TypeConverters(TrainingTypeConverters::class)
abstract class TrainingDatabase : RoomDatabase() {

    abstract fun trainingDao() : TrainingDao

}

val training_migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Training ADD COLUMN test TEXT NOT NULL DEFAULT ''"
        )
    }
}