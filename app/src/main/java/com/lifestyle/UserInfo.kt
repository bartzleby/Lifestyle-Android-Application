package com.lifestyle

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import java.util.*

//Define a global intent variable
//private var mDisplayIntent: Intent? = null

class UserInfo : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    // https://developer.android.com/guide/fragments/communicate#viewmodel
    private val viewModel: SharedViewModel by activityViewModels()

    // Initialize the view model here. One per activity.
    // --can each fragment have one?
    // While initializing, we'll also inject the repository.
    // However, standard view model constructor only takes a context to
    // the activity. We'll need to define our own constructor, but this
    // requires writing our own view model factory.
    private val mLifestyleViewModel: LifestyleViewModel by activityViewModels() {
        LifestyleViewModelFactory((requireContext().applicationContext as LifestyleApplication).repository)
    }

    private var mThumbnailImage: Bitmap? = null

    //Create variables to hold the strings
    private var mFullName: String? = null
    private var mAge: Int? = null
    private var mCity: String? = null
    private var mCountry: String? = "United States"
    private var mHeight: String? = null
    private var mInches: String? = null
    private var mWeight: Int? = null
    private var mSex: String? = null
    private var mActivity: String? = null

    private var mTvFullName: TextView? = null

    private var mButtonSubmit: Button? = null
    private var mButtonCamera: Button? = null
    private var mIvPic: ImageView? = null

    // TODO: these probably shouldn't be spinners:
    private var weightSpinner: Spinner? = null
    private var ageSpinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mIvPic?.drawable?.let {
            outState.putParcelable("bitmap", mThumbnailImage)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val savedBitmap = savedInstanceState?.getParcelable("bitmap", Bitmap::class.java)
        val bitmap = mThumbnailImage
        if (savedBitmap != null || bitmap != null) {
            mIvPic!!.setImageBitmap(savedBitmap ?: bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        //Get the buttons
        mButtonSubmit = view.findViewById(R.id.button_submit)
        mButtonCamera = view.findViewById(R.id.button_take_pic)

        mTvFullName = view.findViewById(R.id.name)
        mIvPic = view.findViewById(R.id.iv_pic)

        mButtonSubmit!!.setOnClickListener(this)
        mButtonCamera!!.setOnClickListener(this)


        // TODO: weight entry should probably be by some other method
        weightSpinner = view.findViewById(R.id.weight_spinner)
        weightSpinner!!.onItemSelectedListener = this
        val ad2 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, weight_list)
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightSpinner!!.setAdapter(ad2)


        ageSpinner = view.findViewById(R.id.age_spinner)
        ageSpinner!!.onItemSelectedListener = this
        val ad4 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, age_list)
        ad4.setDropDownViewResource(android.R.layout.select_dialog_item)
        ageSpinner!!.setAdapter(ad4)

        setupSimpleSpinner(view, R.id.sex_spinner, sex_list)
        setupSimpleSpinner(view, R.id.activity_spinner, activity_list)
        setupSimpleSpinner(view, R.id.feet_spinner, feet_list)
        setupSimpleSpinner(view, R.id.inches_spinner, inches_list)
        setupSimpleSpinner(view, R.id.city_spinner, city_list)

        // TODO: should this be in MainActivity?
        // we start with empty weather database tables:
        mLifestyleViewModel.clearUserData() // do we want this to happen?
        mLifestyleViewModel.clearWeatherData()


        return view
    }

    //
    private fun setupSimpleSpinner(
        v: View,
        spinner_id: Int,
        spinnerItems: List<String>
    ) {
        val spinner = v.findViewById<Spinner>(spinner_id)
        spinner!!.onItemSelectedListener = this
        val ad =
            ArrayAdapter(v.context, android.R.layout.simple_spinner_item, spinnerItems)

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = ad
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0!!.id) {
            R.id.sex_spinner -> {
                mSex = sex_list[p2]
            }
            R.id.activity_spinner -> {
                mActivity = activity_list[p2]
            }
            R.id.age_spinner -> {
                mAge = age_list[p2]
            }
            R.id.weight_spinner -> {
                mWeight = weight_list[p2]
            }
            R.id.feet_spinner -> {
                mHeight = feet_list[p2]
            }
            R.id.inches_spinner -> {
                mInches = inches_list[p2]
                mHeight += mInches
            }
            R.id.city_spinner -> {
                mCity = city_list[p2]
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.button_submit -> {

                mFullName = mTvFullName!!.text.toString()
                if (mFullName.isNullOrBlank()) {
                    //Complain that there's no text
                    Snackbar.make(view, "Please enter your full name.", Snackbar.LENGTH_LONG).show()
                } else {
                    viewModel.selectName(mFullName!!)
                    viewModel.selectGender(sex_list.indexOf(mSex))
                    viewModel.selectActivity(activity_list.indexOf(mActivity))
                    viewModel.selectAge(mAge!!)
                    viewModel.selectWeight(mWeight!!)
                    viewModel.selectHeight(
                        mHeight!![0].digitToInt() * 12 + mInches!!.replace(
                            "\"",
                            ""
                        ).toInt()
                    )
                    //Instantiate the fragment

                    // pass the user data down to the LifestyleRepository via the view model
                    mLifestyleViewModel.setUserData(UserData(mFullName!!, mCity!!, mCountry!!))

                }


            }
            R.id.button_take_pic -> {
                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraActivity.launch(cameraIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(activity, "Unable to launch camera", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val cameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val extras = result.data!!.extras
                mThumbnailImage = extras!!["data"] as Bitmap?

                if (Build.VERSION.SDK_INT >= 33) {
                    mThumbnailImage =
                        result.data!!.getParcelableExtra("data", Bitmap::class.java)
                    mIvPic!!.setImageBitmap(mThumbnailImage)
                } else {
                    mThumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                    mIvPic!!.setImageBitmap(mThumbnailImage)
                }


            }
        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserInfo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic //param1: String, param2: String
        fun newInstance() =
            UserInfo().apply {
                arguments = Bundle().apply {
                    //  putString(ARG_PARAM1, param1)
                    //   putString(ARG_PARAM2, param2)
                }
            }
    }


}
