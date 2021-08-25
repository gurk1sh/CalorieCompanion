package se.umu.cs.guth0028.caloriecompanionapp.userResources

class ActivityLevel {
    /*
    * Class holding an arraylist of different activity levels
    */
    companion object {
        fun newInstance(): ArrayList<String> {
            return arrayListOf("Sedentary (office job)", "Lightly active (1-2 days/week)", "Moderately active (3-5 days/week)", "Very active (6-7 days/week)", "Extra active (2x per day)")
        }
    }
}