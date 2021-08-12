package se.umu.cs.guth0028.caloriecompanionapp.foodResources

class FoodCalorieCalculator(var protein: Float, var fat: Float, var carbohydrates: Float) {
    var calories = (protein*4) + (fat*8) + (carbohydrates*4)
}