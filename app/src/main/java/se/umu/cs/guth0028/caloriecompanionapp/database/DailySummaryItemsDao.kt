package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.DailySummaryFood
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.DailySummaryTraining
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training

@Dao
interface DailySummaryItemsDao { //Data access object that fetches food data from DB with specific queries

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

    @Query("DELETE FROM dailysummarytraining")
    fun clearAllDailySummaryTraining()


    @Query("SELECT * FROM dailysummarytraining")
    fun getDailySummaryTraining() : LiveData<List<DailySummaryTraining>>

    @Update
    fun updateDailySummaryTraining(training: Training)

    @Insert
    fun addDailySummaryTraining(training: Training)

    @Delete
    fun removeDailySummaryTraining(training: Training)

}