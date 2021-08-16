package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.DailySummaryFood
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.DailySummaryTraining
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training

@Dao
interface DailySummaryItemsDao { //Data access object that fetches food data from DB with specific queries

    //Food methods

    @Query("SELECT * FROM dailysummaryfood")
    fun getDailySummaryFoods() : LiveData<List<DailySummaryFood>>

    @Query("SELECT * FROM dailysummaryfood WHERE category=(:category)")
    fun getDailySummaryWithCategory(category: String) : LiveData<List<DailySummaryFood>>

    @Update
    fun updateDailySummaryFoods(dailySummaryFood: DailySummaryFood)

    @Insert
    fun addDailySummaryFoods(dailySummaryFood: DailySummaryFood)

    @Delete
    fun removeDailySummaryFoods(dailySummaryFood: DailySummaryFood)

    @Query("DELETE FROM dailysummaryfood")
    fun clearAllDailySummaryFood()

    @Query("SELECT food.id, name, food.category, protein, fat, carbohydrates FROM food,dailysummaryfood WHERE name=foodName AND dailysummaryfood.category=(:category)")
    fun getFoodsAssociatedWithDS(category: String): LiveData<List<Food>>

    @Query("SELECT dailysummaryfood.weight from dailysummaryfood,food WHERE foodname=(:name) AND foodName=name")
    fun getFoodWeightAssociatedWithDS(name: String): LiveData<Float>
    //Training methods

    @Query("DELETE FROM dailysummarytraining")
    fun clearAllDailySummaryTraining()


    @Query("SELECT * FROM dailysummarytraining")
    fun getDailySummaryTraining() : LiveData<List<DailySummaryTraining>>

    @Update
    fun updateDailySummaryTraining(training: DailySummaryTraining)

    @Insert
    fun addDailySummaryTraining(training: DailySummaryTraining)

    @Delete
    fun removeDailySummaryTraining(training: DailySummaryTraining)

}