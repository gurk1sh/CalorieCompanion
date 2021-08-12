package se.umu.cs.guth0028.caloriecompanionapp.userResources

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import se.umu.cs.guth0028.caloriecompanionapp.R

private const val TAG = "WelcomeFragment"


class WelcomeFragment : Fragment() {
    private lateinit var createUserButton: Button
    private lateinit var firstAndLastNameText: EditText
    private lateinit var genderText: RadioGroup
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var ageText: EditText
    private lateinit var lengthText: EditText
    private lateinit var weightText: EditText
    private lateinit var activityLevelSpinner: Spinner
    private lateinit var user: User

    interface Callbacks {
        fun onMoveToChooseGoal() //interface used to move the user to the foodlistfragment
    }

    private var callbacks: Callbacks? = null

    private val userViewModel: UserViewModel by lazy { //Instantiate a viewmodel object that holds user data
        ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks? //Stash the activity instance hosting the fragment
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null //Null the callbacks because we cannot access the activity or count on it to exist
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = User()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome_user, container, false)
        createUserButton = view.findViewById(R.id.createUserButton)
        firstAndLastNameText = view.findViewById(R.id.firstLastName)
        genderText = view.findViewById(R.id.genderGroup)
        maleRadioButton = view.findViewById(R.id.maleRadioButton)
        femaleRadioButton = view.findViewById(R.id.femaleRadioButton)
        ageText = view.findViewById(R.id.ageText)
        lengthText = view.findViewById(R.id.lengthText)
        weightText = view.findViewById(R.id.weightText)
        activityLevelSpinner = view.findViewById(R.id.activitySpinner)

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_row
        )

        for (activityLevel in ActivityLevel.newInstance()) {
            adapter.add(activityLevel)
        }

        activityLevelSpinner.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()
        addTextChangedListeners(firstAndLastNameText)
        addTextChangedListeners(ageText)
        addTextChangedListeners(lengthText)
        addTextChangedListeners(weightText)
        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        createUserButton.setOnClickListener {
            var textIsNotEmpty = checkForText(firstAndLastNameText)
            textIsNotEmpty = checkForText(ageText)
            textIsNotEmpty = checkForText(weightText)
            textIsNotEmpty = checkForText(lengthText)
            var checkedRadioButton = checkForRadioButton(genderText)

            if (textIsNotEmpty && checkedRadioButton) {
                createUser()
                callbacks?.onMoveToChooseGoal()
            }
        }

        maleRadioButton.setOnClickListener {
            this.user.gender = maleRadioButton.text.toString()
        }

        femaleRadioButton.setOnClickListener {
            this.user.gender = femaleRadioButton.text.toString()
        }
    }

    private fun addTextChangedListeners(editText: EditText) {
        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This space intentionally left blank

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) { //Checks if the textfield is null or empty
                    when (editText) {
                        firstAndLastNameText -> {
                            user.name = s.toString()
                        }
                        ageText -> {
                            user.age = s.toString().toInt()
                        }
                        lengthText -> {
                            user.length = s.toString().toFloat()
                        }
                        weightText -> {
                            user.weight = s.toString().toFloat()
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This space intentionally left blank

            }
        }
        editText.addTextChangedListener(nameWatcher)
    }

    private fun checkForText(editText: EditText) : Boolean {
        if (editText.text.isEmpty()) {
            editText.error = "Error, this cannot be empty."
            return false
        }
        return true
    }

    private fun createUser() {
        this.user.activityLevel = activityLevelSpinner.selectedItem.toString()
        userViewModel.addUser(this.user)

    }

    private fun checkForRadioButton(radioGroup: RadioGroup) : Boolean {
        if (radioGroup.checkedRadioButtonId == -1) {
            maleRadioButton.error = "Error, this cannot be empty"
            femaleRadioButton.error = "Error, this cannot be empty"
            return false
        }
        return true
    }

    companion object {
        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }
}