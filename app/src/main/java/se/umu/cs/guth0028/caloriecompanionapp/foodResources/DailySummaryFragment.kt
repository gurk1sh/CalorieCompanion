package se.umu.cs.guth0028.caloriecompanionapp.foodResources

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
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.guth0028.caloriecompanionapp.DailySummaryFoodViewModel
import se.umu.cs.guth0028.caloriecompanionapp.DailySummaryTrainingViewModel
import se.umu.cs.guth0028.caloriecompanionapp.R
import se.umu.cs.guth0028.caloriecompanionapp.trainingResources.Training
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

    interface Callbacks {
        fun onAddFoodToDailySummary(category: String) //interface used to move the user to the foodlistfragment

        fun onAddTrainingToDailySummary()
    }

    private var callbacks: Callbacks? = null

    private var adapter: DailySummaryFragment.FoodAdapter? = FoodAdapter(emptyList(), emptyList()) //adapter for recyclerview holding the items

    private var trainingAdapter: DailySummaryFragment.TrainingAdapter? = TrainingAdapter(emptyList())

    private var userCalories = 0f

    private var calories_left = 0f

    private var calories_eaten = 0f

    private var calories_burned = 0f

    private lateinit var foodList: MutableMap<String, Float>

    private lateinit var trainingList: MutableMap<String, Float>

    private val userViewModel: UserViewModel by lazy { //Instantiate a viewmodel object that holds user data
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    private val dailySummaryFoodViewModel: DailySummaryFoodViewModel by lazy {
        ViewModelProvider(this).get(DailySummaryFoodViewModel::class.java)
    }

    private val dailySummaryTrainingViewModel: DailySummaryTrainingViewModel by lazy {
        ViewModelProvider(this).get(DailySummaryTrainingViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks? //Stash the activity instance hosting the fragment
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null //Null the callbacks because we cannot access the activity or count on it to exist
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_summary, container, false)
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

    private fun observeFoods(category: String, recyclerView: RecyclerView, textView: TextView){
        dailySummaryFoodViewModel.getDailySummaryFoodsWithCategory(category).observe ( //get all foods added to daily summary cart then append their names to the cardviews
            viewLifecycleOwner,
            { dailySummaryFood ->
                dailySummaryFood?.let {
                    val text = StringBuilder()
                    for (dailySumFood in dailySummaryFood) {
                        text.append(dailySumFood.foodName)
                        text.append(", ")
                    }
                    textView.text = text

        dailySummaryFoodViewModel.getFoodsAssociatedWithDS(category).observe(//fetch all foods that are related to the daily summary food objects
            viewLifecycleOwner,
            { foods ->
                foods?.let {
                    updateUI(foods, dailySummaryFood, recyclerView) //update recycler view
                    var tempCalories = 0f
                    foodList = mutableMapOf<String, Float>()
                    for (food in foods) {
                        dailySummaryFoodViewModel.getFoodWeightAssociatedWithDS(food.name).observe(//get weight of all daily summary foods
                            viewLifecycleOwner,
                            { weight ->
                                weight?.let {
                                    val foodCalories = ((FoodCalorieCalculator(food.protein, food.fat, food.carbohydrates).calories)*weight/100) //calculate calories and update upper UI

                                    tempCalories += foodCalories

                                    foodList[food.name] = foodCalories //map used to calculate calories when item is removed

                                    calories_eaten = tempCalories
                                    calories_left = userCalories - calories_eaten

                                    updateUpperUI()
                                }
                            }
                        )
                    }
                }
            })
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
                    trainingList = mutableMapOf()
                    for (dailySumTraining in training) {
                        text.append(dailySumTraining.trainingName)
                        text.append(", ")
                        tempCals += dailySumTraining.caloriesBurned
                        calories_burned = tempCals
                        trainingList[dailySumTraining.trainingName] = dailySumTraining.caloriesBurned
                    }
                    updateUpperUI()
                    trainingLowerTextView.text = text
                }

            }
        )
    }

    private fun updateUpperUI() {
        caloriesLeft.text = calories_left.toString()
        caloriesEaten.text = calories_eaten.toString()
        caloriesBurned.text = calories_burned.toString()
    }

    private fun initOnClickListeners() {
        addBreakfastButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary("breakfast")
        }

        addLunchButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary("lunch")
        }

        addDinnerButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary("dinner")
        }

        addSnackButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary("snacks")
        }

        addTrainingButton.setOnClickListener {
            callbacks?.onAddTrainingToDailySummary()
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

    private fun updateUI(foods: List<Food>, dailySummaryFoods: List<DailySummaryFood>, recyclerView: RecyclerView) { //update the adapter consisting of food items then update the recyclerview itself
        adapter = FoodAdapter(foods, dailySummaryFoods)
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


        fun bind(food: Food, weight: Float) { //set the textviews and calculate calories using fat,protein,carbs
            val foodCalories = ((FoodCalorieCalculator(food.protein, food.fat, food.carbohydrates).calories)*weight/100)

            Log.d("error", food.name) //Denna metod (bind) anropas två gånger efter jag har tagit bort ett objekt i recyclerviewen.

            foodnameTextView.text = food.name
            calorieTextView.text = foodCalories.toString()
            foodWeight.text = weight.toString()
            categoryTextView.text = food.category
            proteinTextView.text = food.protein.toString()
            fatTextView.text = food.fat.toString()
            carbohydratesTextView.text = food.carbohydrates.toString()

        }

        fun onClickDeleteItem(dailySummaryFood: DailySummaryFood) {
            deleteButton.setOnClickListener {
                dailySummaryFoodViewModel.removeFoodFromDS(dailySummaryFood)
                var tempCalories = 0f

                val tempFood = foodList[dailySummaryFood.foodName]

                if (tempFood != null) {
                    tempCalories += tempFood
                }

                calories_eaten -= tempCalories
                calories_left += tempCalories

                updateUpperUI()

            }
        }
    }

    private inner class FoodAdapter(var foods: List<Food>, var dailySummaryFoods: List<DailySummaryFood>) : RecyclerView.Adapter<DailySummaryFragment.FoodHolder>() {
        //Adapter for recyclerview that tells it to use the list_item_food with the food data
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailySummaryFragment.FoodHolder {
            val view = layoutInflater.inflate(R.layout.list_item_dailysummaryfood, parent, false)
            return FoodHolder(view)
        }

        override fun onBindViewHolder(holder: DailySummaryFragment.FoodHolder, position: Int) { //Displays food data for each food in foods list
            val food = foods[position]

            dailySummaryFoodViewModel.getFoodWeightAssociatedWithDS(food.name).observe(
                viewLifecycleOwner,
                { weight ->
                    weight?.let {
                        holder.bind(food, weight)
                    }
                }
            )

            for (dailySumFood in dailySummaryFoods) {
                holder.onClickDeleteItem(dailySumFood)
            }
        }

        override fun getItemCount(): Int {
            return foods.size
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

                var tempCalories = 0f

                val tempTraining = trainingList[training.trainingName]

                if (tempTraining != null) {
                    tempCalories += tempTraining
                }

                calories_burned -= tempCalories
                updateUpperUI()

            }
        }
    }

    private inner class TrainingAdapter(var training: List<DailySummaryTraining>) : RecyclerView.Adapter<DailySummaryFragment.TrainingHolder>() {
        //Adapter for recyclerview that tells it to use the list_item_food with the food data
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailySummaryFragment.TrainingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_dailysummarytraining, parent, false)
            return TrainingHolder(view)
        }

        override fun onBindViewHolder(holder: DailySummaryFragment.TrainingHolder, position: Int) { //Displays food data for each food in foods list
            val tempTraining = training[position]

            holder.bind(tempTraining)

            for (training in training) {
                holder.onClickDeleteItem(training)
            }
        }

        override fun getItemCount(): Int {
            return training.size
        }
    }

    companion object {
        fun newInstance(): DailySummaryFragment {
            return DailySummaryFragment()
        }
    }
}