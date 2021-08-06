package se.umu.cs.guth0028.caloriecompanionapp

class CalorieCalculator(var protein: Float, var fat: Float, var carbohydrates: Float) {
    var calories = (protein*4) + (fat*8) + (carbohydrates*4)

}