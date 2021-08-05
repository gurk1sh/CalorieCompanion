package se.umu.cs.guth0028.caloriecompanionapp

import androidx.lifecycle.ViewModel

class FoodListViewModel : ViewModel() {

    private val foodRepository = FoodRepository.get()
    val foodListLiveData = foodRepository.getFoods()

}