package se.umu.cs.guth0028.caloriecompanionapp

class ActivityLevel {
    companion object {
        fun newInstance(): ArrayList<String> {
            return arrayListOf("Sedentary", "Lightly active", "Moderately active", "Very active", "Extra active")
        }
    }
}