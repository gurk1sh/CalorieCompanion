package se.umu.cs.guth0028.caloriecompanionapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "FoodListFragment"


class FoodListFragment : Fragment() {

    interface Callbacks {
        fun onFoodSelected(foodId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var foodRecyclerView: RecyclerView
    private var adapter: FoodAdapter? = FoodAdapter(emptyList())

    private val foodListViewModel: FoodListViewModel by lazy {
        ViewModelProvider(this).get(FoodListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
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
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)
        foodRecyclerView = view.findViewById(R.id.food_recycler_view) as RecyclerView
        foodRecyclerView.layoutManager = LinearLayoutManager(context)
        foodRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_food_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_food -> {
                val food = Food()
                callbacks?.onFoodSelected(food.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(foods: List<Food>) {
        adapter = FoodAdapter(foods)
        foodRecyclerView.adapter = adapter
    }

    private inner class FoodHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var food: Food

        private val nameTextView: TextView = itemView.findViewById(R.id.breakfast_food_name)
        private val proteinTextView: TextView = itemView.findViewById(R.id.breakfast_food_protein)
        private val fatTextView: TextView = itemView.findViewById(R.id.breakfast_food_fat)
        private val calorieTextView: TextView = itemView.findViewById(R.id.breakfast_food_calories)
        private val carbohydratesTextView: TextView = itemView.findViewById(R.id.breakfast_food_carbohydrates)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(food: Food) {
            this.food = food
            nameTextView.text = this.food.name
            proteinTextView.text = this.food.protein.toString()
            fatTextView.text = this.food.fat.toString()
            carbohydratesTextView.text = this.food.carbohydrates.toString()
            val calorieCalc = CalorieCalculator(this.food.protein,this.food.fat,this.food.carbohydrates)
            calorieTextView.text = calorieCalc.calories.toString()
        }

        override fun onClick(v: View?) {
            callbacks?.onFoodSelected(food.id)
        }
    }

    private inner class FoodAdapter(var foods: List<Food>) : RecyclerView.Adapter<FoodHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
            val view = layoutInflater.inflate(R.layout.list_item_food, parent, false)
            return FoodHolder(view)
        }

        override fun onBindViewHolder(holder: FoodHolder, position: Int) {
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