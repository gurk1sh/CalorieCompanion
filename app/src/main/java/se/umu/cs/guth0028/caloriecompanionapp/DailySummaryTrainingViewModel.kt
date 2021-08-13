package se.umu.cs.guth0028.caloriecompanionapp

import androidx.lifecycle.ViewModel
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training

class DailySummaryTrainingViewModel : ViewModel() {

    private val dailySummaryRepository = DailySummaryRepository.get()
    val allDailySummaryTraining = dailySummaryRepository.getDailySummaryTraining()


    fun clearDailySummary() {
        dailySummaryRepository.clearDailySummary()
    }

    /*
    * TRAINING METHODS
    * */

    fun addTrainingToDS(training: Training) {
        dailySummaryRepository.addDailySummaryTraining(training)
    }

    fun updateTrainingInDS(training: Training) {
        dailySummaryRepository.updateDailySummaryTraining(training)
    }

    fun removeTrainingFromDS(training: Training) {
        dailySummaryRepository.removeDailySummaryTraining(training)
    }


}