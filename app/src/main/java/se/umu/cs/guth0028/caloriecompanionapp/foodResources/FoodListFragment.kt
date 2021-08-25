package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.graphics.drawable.AnimationDrawable
import android.media.Image
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.guth0028.caloriecompanionapp.R
import se.umu.cs.guth0028.caloriecompanionapp.dailySummaryResources.DailySummaryFood
import se.umu.cs.guth0028.caloriecompanionapp.dailySummaryResources.DailySummaryFoodViewModel

private const val TAG = "FoodListFragment"
private const val ARG_CATEGORY = "category"

class FoodListFragment : Fragment() {


    private lateinit var foodRecyclerView: RecyclerView //Recyclerview holding the food cardviews
    private var adapter: FoodAdapter? = FoodAdapter(emptyList()) //adapter for recyclerview holding the items
    private lateinit var rotateAnimation: AnimationDrawable
    private lateinit var rotateBackAnimation: AnimationDrawable
    private lateinit var foodType: TextView
    private var category: String = ""


    private val foodListViewModel: FoodListViewModel by lazy {
        ViewModelProvider(this).get(FoodListViewModel::class.java)
    }

    private val dailySummaryFoodViewModel: DailySummaryFoodViewModel by lazy {
        ViewModelProvider(this).get(DailySummaryFoodViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false) //inflate food list fragment view inside of the fragment container used in activity_main.xml
        foodRecyclerView = view.findViewById(R.id.food_recycler_view) as RecyclerView
        foodRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for foodrecyclerview custom layout
        foodRecyclerView.adapter = adapter

        foodType = view.findViewById(R.id.food_type)

        category = arguments?.getString(ARG_CATEGORY) as String
        foodType.text = category

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Listen to foodListViewModel which updates the foods in the foodlistfragment every time the view is created

        super.onViewCreated(view, savedInstanceState)
        foodListViewModel.foodListLiveData.observe(
            viewLifecycleOwner,
            { foods ->
                foods?.let {
                    Log.i(TAG, "Got foods ${foods.size}")
                    updateUI(foods)
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_food_list, menu) //inflate food list menu inside of the menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Move the user to the food detail view for adding a new food when they press the plus icon in the menu
        return when (item.itemId) {
            R.id.new_food -> {
                val food = Food()
                val bundle = bundleOf("food_id" to food.id, "category" to category)
                view?.let { Navigation.findNavController(it).navigate(R.id.action_foodListFragment_to_foodFragment, bundle) }
                true
            }
            R.id.home -> {
                view?.let { Navigation.findNavController(it).navigate(R.id.action_foodListFragment_to_dailySummaryFragment) }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(foods: List<Food>) { //update the adapter consisting of food items then update the recyclerview itself
        adapter = FoodAdapter(foods)
        foodRecyclerView.adapter = adapter
    }

    private inner class FoodHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //ViewHolder for updating the recyclerview items UI and onClickListener that moves the view to the food detail view

        private lateinit var food: Food

        private val nameTextView: TextView = itemView.findViewById(R.id.food_name)
        private val categoryTextView: TextView = itemView.findViewById(R.id.food_category)
        private val proteinTextView: TextView = itemView.findViewById(R.id.food_protein)
        private val fatTextView: TextView = itemView.findViewById(R.id.food_fat)
        private val calorieTextView: TextView = itemView.findViewById(R.id.food_calories)
        private val carbohydratesTextView: TextView = itemView.findViewById(R.id.food_carbohydrates)
        private val foodWeight: EditText = itemView.findViewById(R.id.numPicker)
        private val addToDailySummaryButton: ImageButton = itemView.findViewById(R.id.addToDailySummaryButton)
        private lateinit var foodCalorieCalc: FoodCalorieCalculator
        private val rightSlider: ImageView = itemView.findViewById(R.id.rightSlider)
        private val leftSlider: ImageView = itemView.findViewById(R.id.leftSlider)

            init {
            itemView.setOnClickListener(this)
        }

        fun bind(food: Food) { //set the textviews and calculate calories using fat,protein,carbs
            this.food = food
            foodCalorieCalc = FoodCalorieCalculator(this.food.protein,this.food.fat,this.food.carbohydrates)
            nameTextView.text = this.food.name
            categoryTextView.text = this.food.category
            proteinTextView.text = getString(R.string.shortProtein, this.food.protein.toString())
            fatTextView.text = getString(R.string.shortFat, this.food.fat.toString())
            carbohydratesTextView.text = getString(R.string.shortCarbs, this.food.carbohydrates.toString())
            calorieTextView.text = getString(R.string.calories, foodCalorieCalc.calories.toString())

            val slideRight = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.slide_right)
            val slideLeft = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.slide_left)

            addToDailySummaryButton.apply {

                val addingDailySummaryFood = DailySummaryFood()
                var hasBeenPressed = false

                setOnClickListener {

                    val str: String = foodWeight.text.toString()
                    val weight : Float = str.toFloat()
                    addingDailySummaryFood.foodName = food.name
                    addingDailySummaryFood.weight = weight
                    addingDailySummaryFood.category = category
                    addingDailySummaryFood.foodCategory = food.category
                    addingDailySummaryFood.protein = food.protein
                    addingDailySummaryFood.fat = food.fat
                    addingDailySummaryFood.carbs = food.carbohydrates
                    addingDailySummaryFood.calories = (FoodCalorieCalculator(food.protein, food.fat, food.carbohydrates).calories)*(weight/100)

                    if (!hasBeenPressed) { //Play sliding animation and change icon drawable

                        rightSlider.startAnimation(slideRight)
                        setImageResource(R.drawable.rotate)
                        rotateAnimation = addToDailySummaryButton.drawable as AnimationDrawable
                        rotateAnimation.start()

                        dailySummaryFoodViewModel.addFoodToDS(addingDailySummaryFood)

                        hasBeenPressed = !hasBeenPressed
                    } else {
                        leftSlider.startAnimation(slideLeft)
                        setImageResource(R.drawable.rotateback)
                        rotateBackAnimation = addToDailySummaryButton.drawable as AnimationDrawable
                        rotateBackAnimation.start()

                        dailySummaryFoodViewModel.removeFoodFromDS(addingDailySummaryFood)

                        hasBeenPressed = !hasBeenPressed
                    }
                }
            }
        }

        override fun onClick(v: View?) { //Start new fragment when clicking a list item
            val bundle = bundleOf("food_id" to food.id, "category" to category)
            view?.let { Navigation.findNavController(it).navigate(R.id.action_foodListFragment_to_foodFragment, bundle) }
        }
    }

    private inner class FoodAdapter(var foods: List<Food>) : RecyclerView.Adapter<FoodHolder>() {
        //Adapter for recyclerview that tells it to use the list_item_food with the food data
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
            val view = layoutInflater.inflate(R.layout.list_item_food, parent, false)

            return FoodHolder(view)
        }

        override fun onBindViewHolder(holder: FoodHolder, position: Int) { //Displays food data for each food in foods list
            val food = foods[position]
            holder.bind(food)
        }

        override fun getItemCount(): Int {
            return foods.size
        }
    }
}