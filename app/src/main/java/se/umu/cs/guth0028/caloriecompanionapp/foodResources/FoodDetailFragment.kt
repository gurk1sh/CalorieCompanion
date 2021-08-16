package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import se.umu.cs.guth0028.caloriecompanionapp.R
import java.io.File
import java.util.*

private const val ARG_FOOD_ID = "food_id"
private const val ARG_CATEGORY = "category"

class FoodFragment : Fragment() {
    private lateinit var food: Food
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private lateinit var foodName: EditText
    private lateinit var foodCategorySpinner: Spinner
    private lateinit var foodProtein: EditText
    private lateinit var foodFat: EditText
    private lateinit var foodCarbohydrates: EditText
    private lateinit var saveButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var deleteButton: Button
    private lateinit var food_list: ArrayList<Food>

    interface Callbacks {
        fun onFoodSavedOrDeleted(category: String) //interface used to move the user to the foodlistfragment when they either remove or save a new food
    }

    private var callbacks: Callbacks? = null

    private val foodDetailViewModel: FoodDetailViewModel by lazy { //Instantiate a viewmodel object that holds food data
        ViewModelProvider(this).get(FoodDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        food = Food()
        val foodId: UUID = arguments?.getSerializable(ARG_FOOD_ID) as UUID
        foodDetailViewModel.loadFood(foodId) //fetches food from database through viewmodel

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_detailed, container, false) //inflate food fragment view inside of the fragment container used in activity_main.xml
        foodName = view.findViewById(R.id.food_name) as EditText
        foodCategorySpinner =  view.findViewById(R.id.food_category) as Spinner
        foodProtein = view.findViewById(R.id.food_protein) as EditText
        foodFat = view.findViewById(R.id.food_fat) as EditText
        foodCarbohydrates = view.findViewById(R.id.food_carbohydrates) as EditText
        saveButton = view.findViewById(R.id.save_food) as Button
        photoButton = view.findViewById(R.id.food_camera) as ImageButton
        photoView = view.findViewById(R.id.food_photo) as ImageView
        deleteButton = view.findViewById(R.id.delete_food)
        photoFile = foodDetailViewModel.getPhotoFile(food)
        photoUri = FileProvider.getUriForFile(requireActivity(),
        "se.umu.cs.guth0028.caloriecompanionapp.fileprovider",
        photoFile) //Translate the photofile into an uri that can be used by the camera app

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodDetailViewModel.foodLiveData.observe( //Actively looks for changes to the foodrepository and updates the UI accordingly
            viewLifecycleOwner,
             Observer { food ->
                food?.let {
                    this.food = food
                    photoFile = foodDetailViewModel.getPhotoFile(food)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "se.umu.cs.guth0028.caloriecompanionapp.fileprovider",
                        photoFile)
                    updateUI()

                    observeCategoryList()
                }
            })
        observeCategoryList()
    }

    private fun observeCategoryList() {
        foodDetailViewModel.categoryListLivedata.observe(viewLifecycleOwner, Observer { category ->
            val spinnerAdapter =
                context?.let { ArrayAdapter(it, R.layout.spinner_row, category) }
            foodCategorySpinner.adapter = spinnerAdapter

            val foodCategoryPos = spinnerAdapter?.getPosition(food.category)

            if (foodCategoryPos != null) {
                foodCategorySpinner.setSelection(foodCategoryPos)
            }
        })
    }

    override fun onStart() {
        super.onStart()

        addTextChangedListener(foodName)
        addTextChangedListener(foodProtein)
        addTextChangedListener(foodFat)
        addTextChangedListener(foodCarbohydrates)

        saveButton.setOnClickListener { //Saves or updates the food in the database after usage of the save button, also moves user back to food list view
            if (foodName.length() != 0) {
                food.category = foodCategorySpinner.selectedItem.toString()
                val currentFood = foodDetailViewModel.foodLiveData.value?.name
                if (currentFood == foodName.text.toString()) {
                    foodDetailViewModel.updateFood(food)
                    callbacks?.onFoodSavedOrDeleted(arguments?.getSerializable(ARG_CATEGORY) as String)
                } else {
                    foodDetailViewModel.addFood(food)
                    callbacks?.onFoodSavedOrDeleted(arguments?.getSerializable(ARG_CATEGORY) as String)
                }
            }
        }

        deleteButton.setOnClickListener { //removes food from database after usage of the delete button, also moves user back to food list view
            foodDetailViewModel.removeFood(food)
            callbacks?.onFoodSavedOrDeleted(arguments?.getSerializable(ARG_CATEGORY) as String)
        }

        photoButton.apply { //Opens the camera activity if uri permission has been given
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE) //ActivityResultContracts.takePicture()
            //launcher.launch
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION  or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(captureImage)
            }
            updateUI() //refreshes UI so image can be displayed
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks? //Stash the activity instance hosting the fragment
    }

    private fun updateUI() {
        foodName.setText(food.name)
        if (checkForExistingValues()) {
            foodProtein.setText(food.protein.toString())
            foodFat.setText(food.fat.toString())
            foodCarbohydrates.setText(food.carbohydrates.toString())
        }
        updatePhotoView()
    }

    private fun checkForExistingValues() : Boolean {
        if (food.protein == 0f) {
            if (food.fat == 0f) {
                if (food.carbohydrates == 0f) {
                    return false
                }
            }
        }
        return true
    }

    private fun updatePhotoView() { //Updates imageview by setting its resource to a bitmap image version of the result from the camera activity
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null //Null the callbacks because we cannot access the activity or count on it to exist
    }

    private fun addTextChangedListener(editText: EditText) { //Listens to changed text for the fields in the FoodDetailView, actively updates the food object attributes
        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This space intentionally left blank

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) { //Checks if the textfield is null or empty
                    when (editText) {
                        foodName -> {
                            food.name = s.toString()
                        }
                        foodProtein -> {
                            food.protein = s.toString().toFloat()
                        }
                        foodFat -> {
                            food.fat = s.toString().toFloat()
                        }
                        foodCarbohydrates -> {
                            food.carbohydrates = s.toString().toFloat()
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

    companion object { //Is used to create a new instance of FoodFragment which requires a foodId to be used so it knows what to display (name, protein, fat etc)
        fun newInstance(foodId: UUID, category: String): FoodFragment {
            val args = Bundle().apply {
                putSerializable(ARG_FOOD_ID, foodId)
                putSerializable(ARG_CATEGORY, category)
            }
            return FoodFragment().apply {
                arguments = args
            }
        }
    }
}