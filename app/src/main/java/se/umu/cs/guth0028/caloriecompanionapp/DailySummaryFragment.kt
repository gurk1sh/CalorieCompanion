package se.umu.cs.guth0028.caloriecompanionapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.umu.cs.guth0028.caloriecompanionapp.R
import java.util.*

private const val TAG = "DailySummaryFragment"


class DailySummaryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_summary, container, false)
        return view
    }

    companion object {
        fun newInstance(): DailySummaryFragment {
            return DailySummaryFragment()
        }
    }
}