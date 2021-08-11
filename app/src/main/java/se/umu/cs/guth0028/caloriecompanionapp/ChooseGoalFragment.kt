package se.umu.cs.guth0028.caloriecompanionapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment

class ChooseGoalFragment : Fragment() {
private lateinit var letsGoButton: Button

    interface Callbacks {
        fun onMoveToDailySummary() //interface used to move the user to the foodlistfragment
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

        letsGoButton.setOnClickListener {
            callbacks?.onMoveToDailySummary()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_goal, container, false)
        letsGoButton = view.findViewById(R.id.letsGoButton)
        return view
    }

    companion object {
        fun newInstance(): ChooseGoalFragment {
            return ChooseGoalFragment()
        }
    }

}