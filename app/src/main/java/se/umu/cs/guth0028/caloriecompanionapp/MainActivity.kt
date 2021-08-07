package se.umu.cs.guth0028.caloriecompanionapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodFragment
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodListFragment
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.TrainingListFragment
import java.util.*

class MainActivity : AppCompatActivity(), FoodListFragment.Callbacks, FoodFragment.Callbacks, TrainingListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
           val fragment = FoodListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()

            /*val fragment = DailySummaryFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()*/

        }
    }

    override fun onFoodSelected(foodId: UUID) {
       val fragment = FoodFragment.newInstance(foodId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onTrainingSelected() {
        val fragment = TrainingListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onFoodSavedOrDeleted() {
        val fragment = FoodListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}