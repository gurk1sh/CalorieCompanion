package se.umu.cs.guth0028.caloriecompanionapp.trainingResources

import androidx.lifecycle.ViewModel

class TrainingListViewModel : ViewModel() { //ViewModel holding data for training list to avoid unnecessary fetching from database

    private val trainingRepository = TrainingRepository.get()
    val trainingListLiveData = trainingRepository.getAllTraining() //this should maybe be a livedata object and change the getAllTraining to a mutableList

}