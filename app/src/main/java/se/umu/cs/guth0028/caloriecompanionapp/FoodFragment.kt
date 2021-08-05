package se.umu.cs.guth0028.caloriecompanionapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.util.*

private const val ARG_FOOD_ID = "food_id"
private const val REQUEST_DATE = "DialogDate"
private const val DATE_FORMAT = "EEE, MMM, dd"
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2


class FoodFragment : Fragment(),  FragmentResultListener {
    private lateinit var food: Food
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private lateinit var foodName: EditText
    private lateinit var saveButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var deleteButton: Button

    interface Callbacks {
        fun onFoodSavedOrDeleted()
    }

    private var callbacks: Callbacks? = null

    private val foodDetailViewModel: FoodDetailViewModel by lazy {
        ViewModelProvider(this).get(FoodDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        food = Food()
        val foodId: UUID = arguments?.getSerializable(ARG_FOOD_ID) as UUID
        foodDetailViewModel.loadFood(foodId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food, container, false)
        foodName = view.findViewById(R.id.food_name) as EditText
        saveButton = view.findViewById(R.id.save_food) as Button
        photoButton = view.findViewById(R.id.food_camera) as ImageButton
        photoView = view.findViewById(R.id.food_photo) as ImageView
        deleteButton = view.findViewById(R.id.delete_food)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodDetailViewModel.foodLiveData.observe(
            viewLifecycleOwner,
             Observer { food ->
                food?.let {
                    this.food = food
                    photoFile = foodDetailViewModel.getPhotoFile(food)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "se.umu.cs.guth0028.caloriecompanionapp.fileprovider",
                        photoFile)

                    updateUI()
                }
            })
        childFragmentManager.setFragmentResultListener(REQUEST_DATE, viewLifecycleOwner, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_food) {
            callbacks?.onFoodSavedOrDeleted()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This space intentionally left blank

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                food.name = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // This space intentionally left blank

            }
        }
        foodName.addTextChangedListener(titleWatcher)

        saveButton.setOnClickListener {
            if (foodName.length() != 0) {
                foodDetailViewModel.addFood(food)
                callbacks?.onFoodSavedOrDeleted()
            }
        }

        deleteButton.setOnClickListener {
            foodDetailViewModel.removeFood(food)
            callbacks?.onFoodSavedOrDeleted()
        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onStop() {
        super.onStop()
    }

    /*override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }*/

    private fun updateUI() {
        foodName.setText(food.name)

        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(foodId: UUID): FoodFragment {
            val args = Bundle().apply {
                putSerializable(ARG_FOOD_ID, foodId)
            }
            return FoodFragment().apply {
                arguments = args
            }
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        //todo
    }
}