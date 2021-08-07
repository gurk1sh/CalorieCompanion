package se.umu.cs.guth0028.caloriecompanionapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training
import java.util.*

@Dao
interface TrainingDao {

    @Query("SELECT * FROM training")
    fun getAllTraining(): LiveData<List<Training>>

    @Query("SELECT * FROM training WHERE id=(:id)")
    fun getTraining(id: UUID): LiveData<Training?>

    @Update
    fun updateTraining(training: Training)

    @Insert
    fun addTraining(training: Training)

    @Delete
    fun removeTraining(training: Training)

}