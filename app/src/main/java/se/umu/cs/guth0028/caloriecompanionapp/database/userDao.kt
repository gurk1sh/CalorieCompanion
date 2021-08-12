package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.userResources.User
import java.util.*

@Dao
interface UserDao { //Data access object that fetches training data from DB with specific queries

    @Query("SELECT * FROM user WHERE id=(:id)")
    fun getUser(id: UUID?): LiveData<User?>

    @Query("SELECT * FROM user LIMIT 1")
    fun getUserWithoutID(): LiveData<List<User?>>

    @Insert
    fun addUser(user: User)

    @Query("UPDATE user SET goal=(:goal)")
    fun updateUserGoal(goal: String)

    @Query("UPDATE user SET calories=(:calories)")
    fun updateUserCalories(calories: Float)
}