package se.umu.cs.guth0028.caloriecompanionapp.dailySummaryResources

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.guth0028.caloriecompanionapp.R
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.Food
import se.umu.cs.guth0028.caloriecompanionapp.foodResources.FoodCalorieCalculator
import se.umu.cs.guth0028.caloriecompanionapp.userResources.UserViewModel
import java.lang.StringBuilder

private const val TAG = "DailySummaryFragment"

class DailySummaryFragment : Fragment() {
    private lateinit var addBreakfastButton: ImageButton
    private lateinit var addLunchButton: ImageButton
    private lateinit var addDinnerButton: ImageButton
    private lateinit var addSnackButton: ImageButton
    private lateinit var addTrainingButton: ImageButton
    private lateinit var breakfastRecyclerView: RecyclerView
    private lateinit var lunchRecyclerView: RecyclerView
    private lateinit var dinnerRecyclerView: RecyclerView
    private lateinit var snackRecyclerView: RecyclerView
    private lateinit var trainingRecyclerView: RecyclerView
    private lateinit var breakfastCardView: CardView
    private lateinit var lunchCardView: CardView
    private lateinit var dinnerCardView: CardView
    private lateinit var snackCardView: CardView
    private lateinit var trainingCardView: CardView
    private lateinit var caloriesLeft: TextView
    private lateinit var caloriesEaten: TextView
    private lateinit var caloriesBurned: TextView
    private lateinit var breakfastLowerTextView: TextView
    private lateinit var lunchLowerTextView: TextView
    private lateinit var dinnerLowerTextView: TextView
    private lateinit var snackLowerTextView: TextView
    private lateinit var trainingLowerTextView: TextView
    private lateinit var clearItemsButton: Button

    private var adapter: FoodAdapter? = FoodAdapter(emptyList()) //adapter for recyclerview holding the items

    private var trainingAdapter: TrainingAdapter? = TrainingAdapter(emptyList())

    private var userCalories = 0f

    private var calories_left = 0f

    private var calories_eaten = 0f

    private var calories_burned = 0f

    private val userViewModel: UserViewModel by lazy { //Instantiate a viewmodel object that holds user data
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    private val dailySummaryFoodViewModel: DailySummaryFoodViewModel by lazy {
        ViewModelProvider(this).get(DailySummaryFoodViewModel::class.java)
    }

    private val dailySummaryTrainingViewModel: DailySummaryTrainingViewModel by lazy {
        ViewModelProvider(this).get(DailySummaryTrainingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_summary, container, false)

        findViews(view)

        breakfastRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for custom layout
        breakfastRecyclerView.adapter = adapter

        lunchRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for custom layout
        lunchRecyclerView.adapter = adapter

        dinnerRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for custom layout
        dinnerRecyclerView.adapter = adapter

        snackRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for custom layout
        snackRecyclerView.adapter = adapter

        trainingRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for custom layout
        trainingRecyclerView.adapter = trainingAdapter

        return view
    }

    private fun findViews(view: View) {
        addBreakfastButton = view.findViewById(R.id.addFoodButtonOne)
        addLunchButton = view.findViewById(R.id.addFoodButtonTwo)
        addDinnerButton = view.findViewById(R.id.addFoodButtonThree)
        addSnackButton = view.findViewById(R.id.addFoodButtonFour)
        addTrainingButton = view.findViewById(R.id.addFoodButtonFive)
        breakfastRecyclerView = view.findViewById(R.id.breakfastRecyclerView)
        lunchRecyclerView = view.findViewById(R.id.lunchRecyclerView)
        dinnerRecyclerView = view.findViewById(R.id.dinnerRecyclerView)
        snackRecyclerView = view.findViewById(R.id.snackRecyclerView)
        trainingRecyclerView = view.findViewById(R.id.trainingRecyclerView)
        breakfastCardView = view.findViewById(R.id.breakfastCardView)
        lunchCardView = view.findViewById(R.id.lunchCardView)
        dinnerCardView = view.findViewById(R.id.dinnerCardView)
        snackCardView = view.findViewById(R.id.snackCardView)
        trainingCardView = view.findViewById(R.id.trainingCardView)
        caloriesLeft = view.findViewById(R.id.caloriesLeft)
        caloriesEaten = view.findViewById(R.id.caloriesEaten)
        caloriesBurned = view.findViewById(R.id.caloriesBurned)
        clearItemsButton = view.findViewById(R.id.clearItemsButton)

        breakfastLowerTextView = view.findViewById(R.id.lowerBreakfastTextView)
        lunchLowerTextView = view.findViewById(R.id.lowerLunchTextView)
        dinnerLowerTextView = view.findViewById(R.id.lowerDinnerTextView)
        snackLowerTextView = view.findViewById(R.id.lowerSnackTextView)
        trainingLowerTextView = view.findViewById(R.id.lowerTrainingTextView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userIdLiveData.observe( //Load user object and set initial calories at the top
            viewLifecycleOwner,
            { user ->
                user?.let {
                    Log.i("gustaf", "Got users ${user.size}")
                    userCalories = user[0]?.calories!!
                    calories_left = userCalories

                    updateUpperUI()

                    observeFoods("breakfast", breakfastRecyclerView, breakfastLowerTextView) //when the user object is found, init observers for every category and their recyclerview and textviews
                    observeFoods("lunch", lunchRecyclerView, lunchLowerTextView)
                    observeFoods("dinner", dinnerRecyclerView, dinnerLowerTextView)
                    observeFoods("snacks", snackRecyclerView, snackLowerTextView)
                    observeTraining()

                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        initOnClickListeners()
    }

    private fun observeFoods(category: String, recyclerView: RecyclerView, textView: TextView) {
        dailySummaryFoodViewModel.getDailySummaryFoodsWithCategory(category).observe ( //get all foods added to daily summary cart then append their names to the cardviews
            viewLifecycleOwner, { dailySummaryFood ->
                dailySummaryFood?.let {
                val text = StringBuilder()
                var tempCalories = 0f


                for (dailySumFood in dailySummaryFood) {
                    text.append(dailySumFood.foodName)
                    text.append(", ")
                    tempCalories += dailySumFood.calories
                }

                calories_eaten += tempCalories
                calories_left = userCalories - calories_eaten
                updateUpperUI()

                textView.text = text

                updateUI(dailySummaryFood, recyclerView) //update recycler view

            }
        })
    }

    private fun observeTraining() {
        dailySummaryTrainingViewModel.allDailySummaryTraining.observe(
            viewLifecycleOwner,
            { training ->
                training?.let {
                    updateTrainingUI(training)
                    val text = StringBuilder()
                    var tempCals = 0f

                    for (dailySumTraining in training) {
                        text.append(dailySumTraining.trainingName)
                        text.append(", ")
                        tempCals += dailySumTraining.caloriesBurned
                        calories_burned = tempCals

                    }
                    updateTrainingUpperUI()
                    trainingLowerTextView.text = text
                }
            }
        )
    }

    private fun updateTrainingUpperUI() {
        caloriesBurned.text = calories_burned.toInt().toString()
    }

    private fun updateUpperUI() {
        caloriesLeft.text = calories_left.toInt().toString()
        caloriesEaten.text = calories_eaten.toInt().toString()
    }

    private fun initOnClickListeners() {
        addBreakfastButton.setOnClickListener {
            val bundle = bundleOf("category" to "breakfast")
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_dailySummaryFragment_to_foodListFragment, bundle) }
        }

        addLunchButton.setOnClickListener {
            val bundle = bundleOf("category" to "lunch")
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_dailySummaryFragment_to_foodListFragment, bundle) }
        }

        addDinnerButton.setOnClickListener {
            val bundle = bundleOf("category" to "dinner")
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_dailySummaryFragment_to_foodListFragment, bundle) }
        }

        addSnackButton.setOnClickListener {
            val bundle = bundleOf("category" to "snacks")
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_dailySummaryFragment_to_foodListFragment, bundle) }
        }

        addTrainingButton.setOnClickListener {
            val bundle = bundleOf("category" to "training")
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_dailySummaryFragment_to_trainingListFragment, bundle) }
        }

        breakfastCardView.setOnClickListener {
            if (breakfastRecyclerView.visibility == View.GONE) {
                breakfastRecyclerView.visibility = View.VISIBLE
            } else {
                breakfastRecyclerView.visibility = View.GONE
            }
        }

        lunchCardView.setOnClickListener {
            if (lunchRecyclerView.visibility == View.GONE) {
                lunchRecyclerView.visibility = View.VISIBLE
            } else {
                lunchRecyclerView.visibility = View.GONE
            }
        }

        dinnerCardView.setOnClickListener {
            if (dinnerRecyclerView.visibility == View.GONE) {
                dinnerRecyclerView.visibility = View.VISIBLE
            } else {
                dinnerRecyclerView.visibility = View.GONE
            }
        }

        snackCardView.setOnClickListener {
            if (snackRecyclerView.visibility == View.GONE) {
                snackRecyclerView.visibility = View.VISIBLE
            } else {
                snackRecyclerView.visibility = View.GONE
            }
        }

        trainingCardView.setOnClickListener {
            if (trainingRecyclerView.visibility == View.GONE) {
                trainingRecyclerView.visibility = View.VISIBLE
            } else {
                trainingRecyclerView.visibility = View.GONE
            }
        }

        clearItemsButton.setOnClickListener {
            dailySummaryFoodViewModel.clearDailySummary()
            calories_eaten = 0f
            calories_left = userCalories
            calories_burned = 0f
            updateUpperUI()
        }

    }

    private fun updateUI(dailySummaryFoods: List<DailySummaryFood>, recyclerView: RecyclerView) { //update the adapter consisting of food items then update the recyclerview itself
        adapter = FoodAdapter(dailySummaryFoods)
        recyclerView.adapter = adapter
    }

    private fun updateTrainingUI(training: List<DailySummaryTraining>) {
        trainingAdapter = TrainingAdapter(training)
        trainingRecyclerView.adapter = trainingAdapter
    }

    private inner class FoodHolder(view: View) : RecyclerView.ViewHolder(view) {
        //ViewHolder for updating the recyclerview items UI and onClickListener that moves the view to the food detail view

        private val foodnameTextView: TextView = itemView.findViewById(R.id.food_name)
        private val categoryTextView: TextView = itemView.findViewById(R.id.food_category)
        private val proteinTextView: TextView = itemView.findViewById(R.id.food_protein)
        private val fatTextView: TextView = itemView.findViewById(R.id.food_fat)
        private val calorieTextView: TextView = itemView.findViewById(R.id.food_calories)
        private val carbohydratesTextView: TextView = itemView.findViewById(R.id.food_carbohydrates)
        private val foodWeight: TextView = itemView.findViewById(R.id.food_weight)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)


        fun bind(food: DailySummaryFood) { //set the textviews and calculate calories using fat,protein,carbs

            foodnameTextView.text = food.foodName
            calorieTextView.text = food.calories.toString()
            foodWeight.text = food.weight.toString()
            categoryTextView.text = food.foodCategory
            proteinTextView.text = food.protein.toString()
            fatTextView.text = food.fat.toString()
            carbohydratesTextView.text = food.carbs.toString()

        }

        fun onClickDeleteItem(dailySummaryFood: DailySummaryFood) {
            deleteButton.setOnClickListener {
                dailySummaryFoodViewModel.removeFoodFromDS(dailySummaryFood)
                calories_eaten = 0f
                calories_left = userCalories
            }
        }
    }

    private inner class FoodAdapter(var dailySummaryFoods: List<DailySummaryFood>) : RecyclerView.Adapter<FoodHolder>() {
        //Adapter for recyclerview that tells it to use the list_item_food with the food data
        var counter = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
            val view = layoutInflater.inflate(R.layout.list_item_dailysummaryfood, parent, false)
            return FoodHolder(view)
        }

        override fun onBindViewHolder(holder: FoodHolder, position: Int) { //Displays food data for each food in foods list
            val food = dailySummaryFoods[position]

            holder.bind(food)


            holder.onClickDeleteItem(dailySummaryFoods[counter])

            counter++

            if (counter == dailySummaryFoods.size) {
                counter = 0
            }
        }

        override fun getItemCount(): Int {
            return dailySummaryFoods.size
        }
    }

    private inner class TrainingHolder(view: View) : RecyclerView.ViewHolder(view) {
        //ViewHolder for updating the recyclerview items UI and onClickListener that moves the view to the food detail view

        private val trainingTextView: TextView = itemView.findViewById(R.id.training_dailysum_name)
        private val trainingCaloriesTextView: TextView = itemView.findViewById(R.id.training_dailysum_calories)
        private val trainingTimeTextView: TextView = itemView.findViewById(R.id.training_dailysum_time)
        private val deleteTrainingButton: ImageButton = itemView.findViewById(R.id.deleteTrainingButton)


        fun bind(training: DailySummaryTraining) { //set the textviews and calculate calories using fat,protein,carbs
            trainingTextView.text = training.trainingName
            trainingCaloriesTextView.text = getString(R.string.trainingCalories, training.caloriesBurned.toInt().toString())
            trainingTimeTextView.text = getString(R.string.trainingTime, training.length.toString())
        }

        fun onClickDeleteItem(training: DailySummaryTraining) {
            deleteTrainingButton.setOnClickListener {
                dailySummaryTrainingViewModel.removeTrainingFromDS(training)

                calories_burned -= training.caloriesBurned
                updateUpperUI()

            }
        }
    }

    private inner class TrainingAdapter(var training: List<DailySummaryTraining>) : RecyclerView.Adapter<TrainingHolder>() {
        //Adapter for recyclerview that tells it to use the list_item_food with the food data
        var counter = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_dailysummarytraining, parent, false)
            return TrainingHolder(view)
        }

        override fun onBindViewHolder(holder: TrainingHolder, position: Int) { //Displays food data for each food in foods list
            val tempTraining = training[position]

            holder.bind(tempTraining)

            holder.onClickDeleteItem(training[counter])

            counter++

            if (counter == training.size) {
                counter = 0
            }

        }

        override fun getItemCount(): Int {
            return training.size
        }
    }
}