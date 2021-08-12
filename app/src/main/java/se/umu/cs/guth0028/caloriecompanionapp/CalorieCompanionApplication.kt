package se.umu.cs.guth0028.caloriecompanionapp

import android.app.Application
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodRepository
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.TrainingRepository
import se.umu.cs.guth0028.caloriecompanionapp.userResources.UserRepository

class CalorieCompanionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FoodRepository.initialize(this)
        TrainingRepository.initialize(this)
        UserRepository.initialize(this)
    }

}