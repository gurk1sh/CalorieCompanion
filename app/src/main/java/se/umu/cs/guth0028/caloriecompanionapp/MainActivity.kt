package se.umu.cs.guth0028.caloriecompanionapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.DailySummaryFragment
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodFragment
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodListFragment
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.TrainingListFragment
import se.umu.cs.guth0028.caloriecompanionapp.userResources.ChooseGoalFragment
import se.umu.cs.guth0028.caloriecompanionapp.userResources.User
import se.umu.cs.guth0028.caloriecompanionapp.userResources.UserViewModel
import se.umu.cs.guth0028.caloriecompanionapp.userResources.WelcomeFragment
import java.util.UUID

class MainActivity : AppCompatActivity(), FoodListFragment.Callbacks, FoodFragment.Callbacks, TrainingListFragment.Callbacks, DailySummaryFragment.Callbacks, WelcomeFragment.Callbacks, ChooseGoalFragment.Callbacks {
private lateinit var user: User

    private val userViewModel: UserViewModel by lazy { //Instantiate a viewmodel object that holds user data
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        user = User()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        userViewModel.userIdLiveData.observe(
            this,
            { user ->
                user?.let {
                    Log.i("gustaf", "Got users ${user.size}")
                    if (user.isEmpty() && currentFragment == null) { //Check for already existing user, start welcomefragment if not
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
                    userViewModel.userIdLiveData.removeObservers(this)
                }
            }
        )
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