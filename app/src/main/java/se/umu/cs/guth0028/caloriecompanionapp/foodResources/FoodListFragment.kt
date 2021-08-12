package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.guth0028.caloriecompanionapp.R
import java.util.*

private const val TAG = "FoodListFragment"

class FoodListFragment : Fragment() {

    interface Callbacks {
        fun onFoodSelected(foodId: UUID) //interface used to move the user to the foodDetailFragment

        fun onFoodCreated(foodId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var foodRecyclerView: RecyclerView //Recyclerview holding the food cardviews
    private var adapter: FoodAdapter? = FoodAdapter(emptyList()) //adapter for recyclerview holding the items

    private val foodListViewModel: FoodListViewModel by lazy {
        ViewModelProvider(this).get(FoodListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks? //Stash the activity instance hosting the fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onDetach() {
        super.onDetach()
        callbacks = null //Null the callbacks because we cannot access the activity or count on it to exist
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_food_list, menu) //inflate food list menu inside of the menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Move the user to the food detail view for adding a new food when they press the plus icon in the menu
        return when (item.itemId) {
            R.id.new_food -> {
                val food = Food()
                callbacks?.onFoodCreated(food.id)
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
        private val cardView: CardView = itemView.findViewById(R.id.cardView)

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

            addToDailySummaryButton.setOnClickListener {
                var str: String = foodWeight.text.toString()
                var float1 : Float = str.toFloat();
                Log.d("gustaf", "${(float1/100)*foodCalorieCalc.calories }")
                rightSlider.startAnimation(slideRight)
            }
        }

        override fun onClick(v: View?) { //Start new fragment when clicking a list item
            callbacks?.onFoodSelected(food.id)
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

    companion object {
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }
    }
}