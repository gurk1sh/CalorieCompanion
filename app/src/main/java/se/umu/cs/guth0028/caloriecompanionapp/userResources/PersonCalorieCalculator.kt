package se.umu.cs.guth0028.caloriecompanionapp.userResources

class PersonCalorieCalculator() {
    companion object {
        fun newInstance(user: User): Float {
            var bmr:Float = (10f*user.weight)+(6.25f*user.length)-(5*user.age)+5f
            var activityLevels = ActivityLevel.newInstance()
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