package se.umu.cs.guth0028.caloriecompanionapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import se.umu.cs.guth0028.caloriecompanionapp.database.CalorieDatabase
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.DailySummaryFood
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training
import java.util.concurrent.Executors

private const val DATABASE_NAME = "calorie-database"

class DailySummaryRepository private constructor(context: Context) {
//DailySummaryRepository fetching data from database and later pushing it inside of ViewModels

    private val database: CalorieDatabase = Room.databaseBuilder(
        context.applicationContext,
        CalorieDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val dailySummaryDao = database.dailySummaryDao()
    private val executor = Executors.newSingleThreadExecutor() //Executor instance running work from main thread, should be running from separate if bigger database

    fun getDailySummaryFoods(): LiveData<List<DailySummaryFood>> = dailySummaryDao.getDailySummaryFoods()

    fun getDailySummaryFoodsWithCategory(category: String): LiveData<List<DailySummaryFood>> = dailySummaryDao.getDailySummaryWithCategory(category)

    fun addDailySummaryFood(dailySummaryFood: DailySummaryFood) {
        executor.execute {
            dailySummaryDao.addDailySummaryFoods(dailySummaryFood)
        }
    }

    fun updateDailySummaryFood(dailySummaryFood: DailySummaryFood) {
        executor.execute {
            dailySummaryDao.updateDailySummaryFoods(dailySummaryFood)
        }
    }

    fun removeDailySummaryFood(dailySummaryFood: DailySummaryFood) {
        executor.execute {
            dailySummaryDao.removeDailySummaryFoods(dailySummaryFood)
        }
    }

    fun clearDailySummary() {
        executor.execute {
            dailySummaryDao.clearAllDailySummaryFood()
            dailySummaryDao.clearAllDailySummaryTraining()
        }
    }

    fun getDailySummaryTraining() {
        executor.execute {
            dailySummaryDao.getDailySummaryTraining()
        }
    }

    fun addDailySummaryTraining(training: Training) {
        executor.execute {
            dailySummaryDao.addDailySummaryTraining(training)
        }
    }

    fun updateDailySummaryTraining(training: Training) {
        executor.execute {
            dailySummaryDao.updateDailySummaryTraining(training)
        }
    }

    fun removeDailySummaryTraining(training: Training) {
        executor.execute {
            dailySummaryDao.removeDailySummaryTraining(training)
        }
    }

    companion object {
        private var INSTANCE: DailySummaryRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DailySummaryRepository(context)
            }
        }

        fun get(): DailySummaryRepository {
            return INSTANCE ?: throw IllegalStateException("DailySummaryRepository must be initialized")
        }
    }
}