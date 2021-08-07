package se.umu.cs.guth0028.caloriecompanionapp.trainingResources

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import se.umu.cs.guth0028.caloriecompanionapp.database.TrainingDatabase
import se.umu.cs.guth0028.caloriecompanionapp.database.training_migration_1_2
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "training-database"

class TrainingRepository private constructor(context: Context) {

    private val database : TrainingDatabase = Room.databaseBuilder(
        context.applicationContext,
        TrainingDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(training_migration_1_2)
        .build()

    private val trainingDao = database.trainingDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllTraining(): LiveData<List<Training>> = trainingDao.getAllTraining()

    fun getTraining(id: UUID): LiveData<Training?> = trainingDao.getTraining(id)

    fun updateTraining(training: Training) {
        executor.execute {
            trainingDao.updateTraining(training)
        }
    }

    fun addTraining(training: Training) {
        executor.execute {
            trainingDao.addTraining(training)
        }
    }

    fun removeTraining(training: Training) {
        executor.execute {
            trainingDao.removeTraining(training)
        }
    }

    companion object {
        private var INSTANCE: TrainingRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TrainingRepository(context)
            }
        }
        fun get(): TrainingRepository {
            return INSTANCE ?:
            throw IllegalStateException("TrainingRepository must be initialized")
        }
    }
}