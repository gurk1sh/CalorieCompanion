package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import java.util.*

@Dao
interface FoodDao { //Data access object that fetches food data from DB with specific queries

    @Query("SELECT * FROM food")
    fun getFoods(): LiveData<List<Food>> //mutableListOfFood

    @Query("SELECT * FROM food WHERE id=(:id)")
    fun getFood(id: UUID) : LiveData<Food?>

    @Update
    fun updateFood(food: Food)

    @Insert
    fun addFood(food: Food)

    @Delete
    fun removeFood(food: Food)

}