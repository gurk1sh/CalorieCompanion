package se.umu.cs.guth0028.caloriecompanionapp.userResources

import androidx.compose.ui.text.toLowerCase

class PersonCalorieCalculator {
    /*
    * Calculates a recommended daily calorie intake for a person using the Mifflin St Jeor Equation
    */
    companion object {
        fun newInstance(user: User): Float {
            var bmr: Float
            if (user.gender.lowercase() == "male") {
                bmr = (10f*user.weight)+(6.25f*user.length)-(5*user.age)+5f
            } else {
                bmr = (10f*user.weight)+(6.25f*user.length)-(5*user.age)-161f
            }


            val activityLevels = ActivityLevel.newInstance()
            var calories = 0f

            when(user.activityLevel) {
                activityLevels[0] -> calories = bmr*1.2f
                activityLevels[1]-> calories = bmr*1.375f
                activityLevels[2] -> calories = bmr*1.55f
                activityLevels[3] -> calories = bmr*1.725f
                activityLevels[4] -> calories = bmr*1.9f
            }
            return calories
        }
    }



}