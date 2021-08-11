package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.User
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training
import java.util.*

@Dao
interface UserDao { //Data access object that fetches training data from DB with specific queries

    @Query("SELECT * FROM user")
    fun getUser(): LiveData<User?>

    @Query("SELECT accountCreated FROM user WHERE id=(:id)")
    fun getIfUserCreated(id: UUID): LiveData<Boolean?>

    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

}