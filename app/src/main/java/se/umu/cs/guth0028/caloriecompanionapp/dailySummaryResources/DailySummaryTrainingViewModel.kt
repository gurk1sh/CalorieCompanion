package se.umu.cs.guth0028.caloriecompanionapp.dailySummaryResources

import androidx.lifecycle.ViewModel

class DailySummaryTrainingViewModel : ViewModel() {

    private val dailySummaryRepository = DailySummaryRepository.get()
    val allDailySummaryTraining = dailySummaryRepository.getDailySummaryTraining()


    fun clearDailySummary() {
        dailySummaryRepository.clearDailySummary()
    }

    /*
    * TRAINING METHODS
    * */

    fun addTrainingToDS(training: DailySummaryTraining) {
        dailySummaryRepository.addDailySummaryTraining(training)
    }

    fun updateTrainingInDS(training: DailySummaryTraining) {
        dailySummaryRepository.updateDailySummaryTraining(training)
    }

    fun removeTrainingFromDS(training: DailySummaryTraining) {
        dailySummaryRepository.removeDailySummaryTraining(training)
    }


}