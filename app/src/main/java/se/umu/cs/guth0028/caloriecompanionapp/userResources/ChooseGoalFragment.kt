package se.umu.cs.guth0028.caloriecompanionapp.userResources

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import se.umu.cs.guth0028.caloriecompanionapp.R

class ChooseGoalFragment : Fragment() {
    private lateinit var letsGoButton: Button
    private lateinit var user: User
    private lateinit var radioGroup: RadioGroup
    private lateinit var loseWeightButton: RadioButton
    private lateinit var maintainWeightButton: RadioButton
    private lateinit var gainWeightButton: RadioButton
    private lateinit var userCalorieTextView: TextView

    private var calories: Float = 0f

    private val userViewModel: UserViewModel by lazy { //Instantiate a viewmodel object that holds user data
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = User()
    }

    override fun onStart() {
        super.onStart()
        initOnClickListeners()
    }

    private fun initOnClickListeners() {

        letsGoButton.setOnClickListener { //Set user goal, calories and move to daily summary fragment if a goal has been selected
            if (checkForRadioButton(radioGroup)) {
                userViewModel.updateUserGoal(user)
                userViewModel.updateUserCalories(user)
                view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.dailySummaryFragment) }
            }
        }

        /*
        * Update user calories based on their goal, this app assumes you want to gain or lose weight by approx 3,5kg per week
        */

        loseWeightButton.setOnClickListener {
            this.user.goal = loseWeightButton.text.toString()
            this.user.calories = this.calories - 500f
            userCalorieTextView.text = this.user.calories.toInt().toString()
        }

        maintainWeightButton.setOnClickListener {
            this.user.goal = maintainWeightButton.text.toString()
            this.user.calories = this.calories
            userCalorieTextView.text = this.user.calories.toInt().toString()
        }

        gainWeightButton.setOnClickListener {
            this.user.goal = gainWeightButton.text.toString()
            this.user.calories = this.calories + 500f
            userCalorieTextView.text = this.user.calories.toInt().toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_goal, container, false)
        letsGoButton = view.findViewById(R.id.letsGoButton)
        radioGroup = view.findViewById(R.id.goalGroup)
        loseWeightButton = view.findViewById(R.id.loseWeightButton)
        maintainWeightButton = view.findViewById(R.id.maintainWeightButton)
        gainWeightButton = view.findViewById(R.id.gainWeightButton)
        userCalorieTextView = view.findViewById(R.id.userCalorieTextView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*
        * Get a list of users and set the current user to the first entry in list. There should never be two users in the list.
        */
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userLiveData.observe(
            viewLifecycleOwner,
            { user ->
                user?.let {
                    this.user = user[0]!!
                    this.calories = PersonCalorieCalculator.newInstance(this.user)
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        if (user.calories == 0f) {
            user.calories = this.calories
        }

        userViewModel.updateUserGoal(user)
        userViewModel.updateUserCalories(user)
    }

    private fun checkForRadioButton(radioGroup: RadioGroup) : Boolean {
        if (radioGroup.checkedRadioButtonId == -1) {
            loseWeightButton.error = "Error, this cannot be empty"
            maintainWeightButton.error = "Error, this cannot be empty"
            gainWeightButton.error = "Error, this cannot be empty"
            return false
        }
        return true
    }
}