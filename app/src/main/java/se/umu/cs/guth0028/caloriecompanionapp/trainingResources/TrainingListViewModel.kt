package se.umu.cs.guth0028.caloriecompanionapp.trainingResources

import androidx.lifecycle.ViewModel

class TrainingListViewModel : ViewModel() {

    private val trainingRepository = TrainingRepository.get()
    val trainingListLiveData = trainingRepository.getAllTraining()

}