package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class FoodDetailViewModel : ViewModel() { //Viewmodel holding food objects to avoid unnecessary fetching from database

    private val foodRepository = FoodRepository.get()
    private val foodIdLiveData = MutableLiveData<UUID>()
    val allFoods = foodRepository.getFoods()

    var foodLiveData: LiveData<Food?> =
        Transformations.switchMap(foodIdLiveData) { foodId ->
            foodRepository.getFood(foodId)
        }

    fun loadFood(foodId: UUID) {
        foodIdLiveData.value = foodId
    }

    fun addFood(food: Food) {
        foodRepository.addFood(food)
    }

    fun updateFood(food: Food) {
        foodRepository.updateFood(food)
    }

    fun removeFood(food: Food) {
        foodRepository.removeFood(food)
    }

    fun getPhotoFile(food: Food): File {
        return foodRepository.getPhotoFile(food)
    }

    val categoryListLivedata = foodRepository.getCategories() //this should maybe be a livedata object and change the getFoods to a mutableList



}