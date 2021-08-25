package se.umu.cs.guth0028.caloriecompanionapp.dailySummaryResources

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food

class DailySummaryFoodViewModel : ViewModel() {

    private val dailySummaryRepository = DailySummaryRepository.get()
    val allDailySummaryFoods = dailySummaryRepository.getDailySummaryFoods()
    val allDailySummaryTraining = dailySummaryRepository.getDailySummaryTraining()

    fun clearDailySummary() {
        dailySummaryRepository.clearDailySummary()
    }

    /*
    * FOOD METHODS
    * */

    fun addFoodToDS(dailySummaryFood: DailySummaryFood) {
        dailySummaryRepository.addDailySummaryFood(dailySummaryFood)
    }

    fun removeFoodFromDS(dailySummaryFood: DailySummaryFood) {
        dailySummaryRepository.removeDailySummaryFood(dailySummaryFood)
    }

    fun getDailySummaryFoodsWithCategory(category: String) : LiveData<List<DailySummaryFood>> {
        return dailySummaryRepository.getDailySummaryFoodsWithCategory(category)
    }
}