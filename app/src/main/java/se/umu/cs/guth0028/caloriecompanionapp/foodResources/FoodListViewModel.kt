package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import androidx.lifecycle.ViewModel

class FoodListViewModel : ViewModel() { //ViewModel holding data for food list to avoid unnecessary fetching from database

    private val foodRepository = FoodRepository.get()
    val foodListLiveData = foodRepository.getFoods()//this should maybe be a livedata object and change the getFoods to a mutableList
}