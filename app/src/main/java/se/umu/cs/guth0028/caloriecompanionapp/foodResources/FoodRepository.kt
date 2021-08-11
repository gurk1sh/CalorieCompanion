package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import se.umu.cs.guth0028.caloriecompanionapp.database.CalorieDatabase
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "calorie-database"

class FoodRepository private constructor(context: Context) {
//Food Repository fetching data from database and later pushing it inside of food ViewModels

    private val database : CalorieDatabase = Room.databaseBuilder(
        context.applicationContext,
        CalorieDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val foodDao = database.calorieDao()
    private val executor = Executors.newSingleThreadExecutor() //Executor instance running work from main thread, should be running from separate if bigger database
    private val filesDir = context.applicationContext.filesDir //File storage for photos

    fun getFoods(): LiveData<List<Food>> = foodDao.getFoods()

    fun getFood(id: UUID): LiveData<Food?> = foodDao.getFood(id)

    fun getCategories(): LiveData<List<String>> = foodDao.getCategories()

    fun updateFood(food: Food) {
        executor.execute {
            foodDao.updateFood(food)
        }
    }

    fun addFood(food: Food) {
        executor.execute {
            foodDao.addFood(food)
        }
    }

    fun removeFood(food: Food) {
        executor.execute {
            foodDao.removeFood(food)
        }
    }

    fun getPhotoFile(food: Food): File = File(filesDir, food.photoFileName)

    companion object {
        private var INSTANCE: FoodRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FoodRepository(context)
            }
        }
        fun get(): FoodRepository {
            return INSTANCE ?:
            throw IllegalStateException("FoodRepository must be initialized")
        }
    }
}