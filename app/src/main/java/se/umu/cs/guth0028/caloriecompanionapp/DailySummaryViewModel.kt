package se.umu.cs.guth0028.caloriecompanionapp

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class DailySummaryViewModel : BaseObservable() {

    var calories = 1000f


    @get:Bindable
    val caloriesEaten: String
        get() = 1000f.toString()

    @get:Bindable
    val caloriesLeft: String
        get() = 1500f.toString()

    @get:Bindable
    val caloriesBurned: String
        get() = 100f.toString()

}