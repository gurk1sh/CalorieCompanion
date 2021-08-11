package se.umu.cs.guth0028.caloriecompanionapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodFragment
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodListFragment
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.TrainingListFragment
import java.util.UUID

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "food_database"

class MainActivity : AppCompatActivity(), FoodListFragment.Callbacks, FoodFragment.Callbacks, TrainingListFragment.Callbacks, DailySummaryFragment.Callbacks, WelcomeFragment.Callbacks, ChooseGoalFragment.Callbacks {
private lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        user = User()


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)


        val welcomeViewModel: WelcomeViewModel by lazy { //Instantiate a viewmodel object that holds user data
            ViewModelProvider(this).get(WelcomeViewModel::class.java)
        }

        welcomeViewModel.userLiveData.observe( //Set user object to one in database
            this,
            Observer { user ->
                user?.let {
                    this.user = user

                }
            })


        if (!this.user.accountCreated) { //Check for already existing user, start welcomefragment if not
           val welcomeFragment = WelcomeFragment.newInstance()
               supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, welcomeFragment)
                .commit()
        } else {
             val fragment = DailySummaryFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }








        //add check for boolean
       if (currentFragment == null) {
           /* val fragment = WelcomeFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()*/

       /* val fragment = DailySummaryFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()*/

           /*val fragment = FoodListFragment.newInstance()
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

    override fun onFoodCreated(foodId: UUID) {
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

    override fun onAddFoodToDailySummary() {
        val fragment = FoodListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onAddTrainingToDailySummary() {
        val fragment = TrainingListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onMoveToChooseGoal() {
        val fragment = ChooseGoalFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onMoveToDailySummary() {
        val fragment = DailySummaryFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}