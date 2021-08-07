package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import se.umu.cs.guth0028.caloriecompanionapp.database.FoodDatabase
import se.umu.cs.guth0028.caloriecompanionapp.database.food_migration_1_2
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "food-database"

class FoodRepository private constructor(context: Context) {

    private val database : FoodDatabase = Room.databaseBuilder(
        context.applicationContext,
        FoodDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(food_migration_1_2)
        .build()

    private val foodDao = database.foodDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getFoods(): LiveData<List<Food>> = foodDao.getFoods()

    fun getFood(id: UUID): LiveData<Food?> = foodDao.getFood(id)

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