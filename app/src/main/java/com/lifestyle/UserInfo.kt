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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"




//Define a bitmap
private var mThumbnailImage: Bitmap? = null

//Define a global intent variable
private var mDisplayIntent: Intent? = null


class UserInfo : Fragment(),  View.OnClickListener {
    // TODO: Rename and change types of parameters
   // private var param1: String? = null
   //private var param2: String? = null
    //Create variables to hold the strings
    private var mFullName: String? = null
    private var mAge: String? = null
    private var mCity: String? = null
    private var mCountry: String? = null
    private var mHeight: String? = null
    private var mWeight: String? = null
    private var mSex: String? = null

    private var mTvFullName: TextView? = null
    private var mEtFullName: EditText? = null
    private var mTvAge: TextView? = null
    private var mTvCity: TextView? = null
    private var mTvCountry: TextView? = null
    private var mTvHeight: TextView? = null
    private var mTvWeight: TextView? = null
    private var mTvSex: TextView? = null
    private var mTvActivity_level: TextView? = null
    private var mButtonSubmit: Button? = null
    private var mButtonCamera: Button? = null
    private var mIvPic: ImageView? = null

    private var mDisplayIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //    arguments?.let {
       //     param1 = it.getString(ARG_PARAM1)
     //       param2 = it.getString(ARG_PARAM2)
     //   }

     /**   mButtonSubmit = findViewById(R.id.button_submit)
        mButtonCamera = findViewById(R.id.button_take_pic)
        mButtonSubmit!!.setOnClickListener(this)
        mButtonCamera!!.setOnClickListener(this) */





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

        return view;

    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.button_submit -> {

                mFullName = mTvFullName!!.text.toString()
               if (mFullName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(activity, "Enter a name first!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                   /*
                   val BmrFragment = BmrFragment()
                   activity!!.supportFragmentManager.beginTransaction().replace(R.id.user_frag_container, BmrFragment, "bmr_frag").addToBackStack(null).commit()
                    */
                   R.id.BmrFragment?.let {
                       activity!!.supportFragmentManager.commit {
                           activity!!.findNavController(R.id.user_frag_container)
                               .navigate(R.id.BmrFragment)
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
