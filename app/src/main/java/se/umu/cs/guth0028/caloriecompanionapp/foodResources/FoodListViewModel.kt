package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import androidx.lifecycle.ViewModel

class FoodListViewModel : ViewModel() {

    private val foodRepository = FoodRepository.get()
    val foodListLiveData = foodRepository.getFoods()//this should maybe be a livedata object and change the getFoods to a mutableList
}