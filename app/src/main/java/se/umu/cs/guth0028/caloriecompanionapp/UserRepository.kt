package se.umu.cs.guth0028.caloriecompanionapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import se.umu.cs.guth0028.caloriecompanionapp.database.CalorieDatabase
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "calorie-database"

class UserRepository private constructor(context: Context) {

    private val database : CalorieDatabase = Room.databaseBuilder(
        context.applicationContext,
        CalorieDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset("databases/calorieDatabase.db").build()

    private val userDao = database.userDao()
    private val executor = Executors.newSingleThreadExecutor() //Executor instance running work from main thread, should be running from separate if bigger database

    fun getUser(): LiveData<User?> = userDao.getUser()

    fun getIfUserCreated(id: UUID): LiveData<Boolean?> = userDao.getIfUserCreated(id)

    fun updateUser(user: User) {
        executor.execute {
            userDao.updateUser(user)
        }
    }

    fun addUser(user: User) {
        executor.execute {
            userDao.addUser(user)
        }
    }

    companion object {
        private var INSTANCE: UserRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = UserRepository(context)
            }
        }
        fun get(): UserRepository {
            return INSTANCE ?:
            throw IllegalStateException("UserRepository must be initialized")
        }
    }

}