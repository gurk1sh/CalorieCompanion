package se.umu.cs.guth0028.caloriecompanionapp

import android.app.Application

class CalorieCompanionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FoodRepository.initialize(this)
        TrainingRepository.initialize(this)
    }

}