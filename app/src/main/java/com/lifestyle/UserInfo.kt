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
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"




//Define a bitmap
private var mThumbnailImage: Bitmap? = null

//Define a global intent variable
//private var mDisplayIntent: Intent? = null


class UserInfo : Fragment(),  View.OnClickListener,  AdapterView.OnItemSelectedListener  {
    // TODO: Rename and change types of parameters
   // private var param1: String? = null
   //private var param2: String? = null
    //Create variables to hold the strings
    private var mFullName: String? = null
    private var mAge: Int? = null
    private var mCity: String? = null
    private var mCountry: String? = null
    private var mHeight: String? = null
    private var mWeight: Int? = null
    private var mSex: String? = null
    private var mActivity: String? = null

    private var mTvFullName: TextView? = null
    //private var mEtFullName: EditText? = null
   // private var mTvAge: TextView? = null
  //  private var mTvCity: TextView? = null
   // private var mTvCountry: TextView? = null
   // private var mTvHeight: TextView? = null
   // private var mTvWeight: TextView? = null
   // private var mTvSex: TextView? = null
   // private var mTvActivityLevel: TextView? = null
    private var mButtonSubmit: Button? = null
    private var mButtonCamera: Button? = null
    private var mIvPic: ImageView? = null
   // private var agePicker: NumberPicker? = null
    private var sexSpinner: Spinner? = null
    private var weightSpinner: Spinner? = null
    private var activitySpinner: Spinner? = null
    private var ageSpinner: Spinner? = null
    private var feetSpinner: Spinner? = null
    private var inchSpinner: Spinner? = null
    //private var weightPicker: NumberPicker? = null

   // private var mDisplayIntent: Intent? = null
    private val sex_list : List<String> = listOf("Male", "Female")
    private val activity_list : List<String> = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active")
    private val  age_list = (13.. 99).toList()
    private val weight_list = (85.. 500).toList()
    private val feet_list : List<String> = listOf("1'", "2'", "3'", "4'", "5'", "6'", "7'")
    private val inches_list : List<String> = listOf("0\"", "1\"", "2\"", "3\"", "4\"", "5\"", "6\"", "7\"", "8\"", "9\"", "10\"", "11\"")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //    arguments?.let {
       //     param1 = it.getString(ARG_PARAM1)
     //       param2 = it.getString(ARG_PARAM2)
     //   }

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
        mButtonSubmit!!.setOnClickListener(this)
        mButtonCamera!!.setOnClickListener(this)
        // mTvSex = view.findViewById(R.id.sex_view)
        //mTvActivityLevel = view.findViewById(R.id.activity_level_view)

      /*  //age picker
        agePicker = view.findViewById(R.id.number_picker_age)
        agePicker!!.setMaxValue(100)
        agePicker!!.setMinValue(10)
        agePicker!!.setWrapSelectorWheel(true)
        agePicker!!.setOnValueChangedListener(OnValueChangeListener { picker, oldVal, newVal ->
            mAge = newVal

        })*/

  /*     //weight picker
        weightPicker = view.findViewById(R.id.number_picker_weight)
        weightPicker!!.setMaxValue(600)
        weightPicker!!.setMinValue(95)
        weightPicker!!.setWrapSelectorWheel(true)
        weightPicker!!.setOnValueChangedListener(OnValueChangeListener { picker, oldVal, newVal ->
            mWeight = newVal

        }) */

        //sex drop down
        sexSpinner = view.findViewById(R.id.sex_spinner)
        sexSpinner!!.setOnItemSelectedListener(this)
        val ad = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, sex_list)
        ad.setDropDownViewResource(android.R.layout.select_dialog_item)
        sexSpinner!!.setAdapter(ad)

        //weight drop down
        weightSpinner = view.findViewById(R.id.weight_spinner)
        weightSpinner!!.setOnItemSelectedListener(this)
        val ad2 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, weight_list)
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightSpinner!!.setAdapter(ad2)

        //activity drop down
        activitySpinner = view.findViewById(R.id.activity_spinner)
        activitySpinner!!.setOnItemSelectedListener(this)
        val ad3 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, activity_list)
        ad3.setDropDownViewResource(android.R.layout.select_dialog_item)
        activitySpinner!!.setAdapter(ad3)

        //age drop down
        ageSpinner = view.findViewById(R.id.age_spinner)
        ageSpinner!!.setOnItemSelectedListener(this)
        val ad4 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, age_list)
        ad4.setDropDownViewResource(android.R.layout.select_dialog_item)
        ageSpinner!!.setAdapter(ad4)

        //feet drop down
        feetSpinner = view.findViewById(R.id.feet_spinner)
        feetSpinner!!.setOnItemSelectedListener(this)
        val ad5 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, feet_list)
        ad5.setDropDownViewResource(android.R.layout.select_dialog_item)
        feetSpinner!!.setAdapter(ad5)

        //feet drop down
        inchSpinner = view.findViewById(R.id.inches_spinner)
        inchSpinner!!.setOnItemSelectedListener(this)
        val ad6 = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, inches_list)
        ad6.setDropDownViewResource(android.R.layout.select_dialog_item)
        inchSpinner!!.setAdapter(ad6)


        return view

    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //Toast.makeText(view!!.context, items[p2], Toast.LENGTH_SHORT).show();
       when (p0!!.id) {
            R.id.sex_spinner-> {
              //  mTvSex!!.text = sex_list[p2]
                mSex = sex_list[p2]
            }
            R.id.activity_spinner-> {
               // mTvActivityLevel!!.text = activity_list[p2]
                mActivity = activity_list[p2]

            }
           R.id.age_spinner-> {
               // mTvActivityLevel!!.text = activity_list[p2]
               mAge = age_list[p2]

           }
           R.id.weight_spinner-> {
               // mTvActivityLevel!!.text = activity_list[p2]
               mWeight= weight_list[p2]

           }
           R.id.feet_spinner-> {
               // mTvActivityLevel!!.text = activity_list[p2]
               mHeight= feet_list[p2]

           }
           R.id.inches_spinner-> {
               // mTvActivityLevel!!.text = activity_list[p2]
               mHeight = mHeight + inches_list[p2]

           }
            }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //empty
    }


    override fun onClick(view: View) {

        when (view.id) {
            R.id.button_submit -> {

                mFullName = mTvFullName!!.text.toString()
                if (mFullName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(activity, "Enter a name first!", Toast.LENGTH_SHORT)
                        .show()
                    Toast.makeText(activity, "this is your height " + mHeight, Toast.LENGTH_SHORT).show()
                } else { //go to next fragment - bmr
                    R.id.BmrFragment?.let {
                        activity!!.supportFragmentManager.commit {
                            findNavController().navigate(R.id.BmrFragment)
                        }
                    }
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
                mIvPic = view!!.findViewById(R.id.iv_pic) as ImageView
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
