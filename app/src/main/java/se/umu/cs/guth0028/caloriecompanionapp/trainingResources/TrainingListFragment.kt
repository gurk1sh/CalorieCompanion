package se.umu.cs.guth0028.caloriecompanionapp.trainingResources

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.guth0028.caloriecompanionapp.R
import java.util.*

private const val TAG = "TrainingListFragment"


class TrainingListFragment : Fragment() {

    interface Callbacks {
        fun onTrainingSelected()
    }

    private var callbacks: Callbacks? = null

    private lateinit var trainingRecyclerView: RecyclerView
    private var adapter: TrainingAdapter? = TrainingAdapter(emptyList())

    private val trainingListViewModel: TrainingListViewModel by lazy {
        ViewModelProvider(this).get(TrainingListViewModel::class.java)
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
        val view = inflater.inflate(R.layout.fragment_training_list, container, false)
        //inflate training list fragment view inside of the fragment container used in activity_main.xml
        trainingRecyclerView = view.findViewById(R.id.training_recycler_view) as RecyclerView
        trainingRecyclerView.layoutManager = LinearLayoutManager(context) //Layoutmanager for foodrecyclerview custom layout
        trainingRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Listen to trainingListViewModel which updates the training in the traininglistfragment every time the view is created

        super.onViewCreated(view, savedInstanceState)
        trainingListViewModel.trainingListLiveData.observe(
            viewLifecycleOwner,
            { training ->
                training?.let {
                    Log.i(TAG, "Got training ${training.size}")
                    updateUI(training)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null //Null the callbacks because we cannot access the activity or count on it to exist
    }

    private fun updateUI(training: List<Training>) { //update the adapter consisting of training items then update the recyclerview itself
        adapter = TrainingAdapter(training)
        trainingRecyclerView.adapter = adapter
    }

    private inner class TrainingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //ViewHolder for updating the recyclerview items UI and onClickListener that moves the view to the training detail view
        private lateinit var training: Training

        private val nameTextView: TextView = itemView.findViewById(R.id.training_name)
        private val proteinTextView: TextView = itemView.findViewById(R.id.training_calories)
        private val fatTextView: TextView = itemView.findViewById(R.id.training_time)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(training: Training) { //set the text for the textviews
            this.training = training
            nameTextView.text = this.training.name
            proteinTextView.text = this.training.caloriesBurned.toString()
            fatTextView.text = this.training.time.toString()
        }

        override fun onClick(v: View?) {
            callbacks?.onTrainingSelected()
        }
    }

    private inner class TrainingAdapter(var training: List<Training>) : RecyclerView.Adapter<TrainingHolder>() {
        //Adapter for recyclerview that tells it to use the list_item_training with the food data
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_training, parent, false)
            return TrainingHolder(view)
        }

        override fun onBindViewHolder(holder: TrainingHolder, position: Int) {
            val trainingObj = training[position]
            holder.bind(trainingObj)
        }

        override fun getItemCount(): Int {
            return training.size
        }
    }

    companion object {
        fun newInstance(): TrainingListFragment {
            return TrainingListFragment()
        }
    }
}