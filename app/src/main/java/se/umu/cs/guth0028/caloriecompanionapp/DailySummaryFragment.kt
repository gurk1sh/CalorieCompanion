package se.umu.cs.guth0028.caloriecompanionapp

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment

private const val TAG = "DailySummaryFragment"


class DailySummaryFragment : Fragment() {
    private lateinit var addBreakfastButton: ImageButton
    private lateinit var addLunchButton: ImageButton
    private lateinit var addDinnerButton: ImageButton
    private lateinit var addSnackButton: ImageButton
    private lateinit var addTrainingButton: ImageButton

    interface Callbacks {
        fun onAddFoodToDailySummary() //interface used to move the user to the foodlistfragment

        fun onAddTrainingToDailySummary()
    }

    private var callbacks: Callbacks? = null

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
        return view
    }

    private fun initOnClickListeners() {
        addBreakfastButton.setOnClickListener { //removes food from database after usage of the delete button, also moves user back to food list view
            callbacks?.onAddFoodToDailySummary()
        }

        addLunchButton.setOnClickListener { //removes food from database after usage of the delete button, also moves user back to food list view
            callbacks?.onAddFoodToDailySummary()
        }

        addDinnerButton.setOnClickListener { //removes food from database after usage of the delete button, also moves user back to food list view
            callbacks?.onAddFoodToDailySummary()
        }

        addSnackButton.setOnClickListener { //removes food from database after usage of the delete button, also moves user back to food list view
            callbacks?.onAddFoodToDailySummary()
        }

        addTrainingButton.setOnClickListener { //removes food from database after usage of the delete button, also moves user back to food list view
            callbacks?.onAddTrainingToDailySummary()
        }
    }

    companion object {
        fun newInstance(): DailySummaryFragment {
            return DailySummaryFragment()
        }
    }
}