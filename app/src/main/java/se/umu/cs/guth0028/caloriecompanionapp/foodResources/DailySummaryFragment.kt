package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.guth0028.caloriecompanionapp.DailySummaryFoodViewModel
import se.umu.cs.guth0028.caloriecompanionapp.R
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
    private lateinit var breakfastLowerTextView: TextView
    private lateinit var lunchLowerTextView: TextView
    private lateinit var dinnerLowerTextView: TextView
    private lateinit var snackLowerTextView: TextView
    private lateinit var clearItemsButton: Button

    interface Callbacks {
        fun onAddFoodToDailySummary() //interface used to move the user to the foodlistfragment

        fun onAddTrainingToDailySummary()
    }

    private var callbacks: Callbacks? = null

    private var adapter: DailySummaryFragment.FoodAdapter? = FoodAdapter(emptyList()) //adapter for recyclerview holding the items

    private val userViewModel: UserViewModel by lazy { //Instantiate a viewmodel object that holds user data
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    private val dailySummaryFoodViewModel: DailySummaryFoodViewModel by lazy {
        ViewModelProvider(this).get(DailySummaryFoodViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks? //Stash the activity instance hosting the fragment
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null //Null the callbacks because we cannot access the activity or count on it to exist
    }

    override fun onStart() {
        super.onStart()

        initOnClickListeners()
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
        clearItemsButton = view.findViewById(R.id.clearItemsButton)

        breakfastLowerTextView = view.findViewById(R.id.lowerBreakfastTextView)
        lunchLowerTextView = view.findViewById(R.id.lowerLunchTextView)
        dinnerLowerTextView = view.findViewById(R.id.lowerDinnerTextView)
        snackLowerTextView = view.findViewById(R.id.lowerSnackTextView)

        breakfastRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for foodrecyclerview custom layout
        breakfastRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeholderFood = Food()

        placeholderFood.name = "test"
        placeholderFood.category = "test"
        placeholderFood.protein = 1f
        placeholderFood.fat = 5f
        placeholderFood.carbohydrates = 10f

        val placeholderFoods = listOf(placeholderFood)

        updateUI(placeholderFoods, breakfastRecyclerView)

        userViewModel.userIdLiveData.observe(
            viewLifecycleOwner,
            { user ->
                user?.let {
                    Log.i("gustaf", "Got users ${user.size}")
                    //caloriesLeft.text = user[0]?.calories.toString()
                }
            }
        )

        dailySummaryFoodViewModel.getDailySummaryFoodsWithCategory("breakfast").observe (
            viewLifecycleOwner,
            { dailySummaryFood ->
                dailySummaryFood?.let {
                val text = StringBuilder()
                    for (dailySumFood in dailySummaryFood) {
                        text.append(dailySumFood.foodName)
                        text.append(", ")
                    }
                breakfastLowerTextView.text = text
            }
        })
    }

    private fun initOnClickListeners() {
        addBreakfastButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary()
        }

        addLunchButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary()
        }

        addDinnerButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary()
        }

        addSnackButton.setOnClickListener {
            callbacks?.onAddFoodToDailySummary()
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

        clearItemsButton.setOnClickListener {
            dailySummaryFoodViewModel.clearDailySummary()
        }

    }

    private fun updateUI(foods: List<Food>, recyclerView: RecyclerView) { //update the adapter consisting of food items then update the recyclerview itself
        adapter = FoodAdapter(foods)
        recyclerView.adapter = adapter
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



        fun bind(food: Food) { //set the textviews and calculate calories using fat,protein,carbs
            foodnameTextView.text = food.name
            calorieTextView.text = food.name
            foodWeight.text = food.name
            categoryTextView.text = food.category
            proteinTextView.text = food.protein.toString()
            fatTextView.text = food.fat.toString()
            carbohydratesTextView.text = food.carbohydrates.toString()

        }
    }

    private inner class FoodAdapter(var foods: List<Food>) : RecyclerView.Adapter<DailySummaryFragment.FoodHolder>() {
        //Adapter for recyclerview that tells it to use the list_item_food with the food data
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailySummaryFragment.FoodHolder {
            val view = layoutInflater.inflate(R.layout.list_item_dailysummaryfood, parent, false)
            return FoodHolder(view)
        }

        override fun onBindViewHolder(holder: DailySummaryFragment.FoodHolder, position: Int) { //Displays food data for each food in foods list
            val food = foods[position]
            holder.bind(food)
        }

        override fun getItemCount(): Int {
            return foods.size
        }
    }

    companion object {
        fun newInstance(): DailySummaryFragment {
            return DailySummaryFragment()
        }
    }
}