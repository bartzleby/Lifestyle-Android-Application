package com.lifestyle

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.content.ActivityNotFoundException
import android.widget.Toast
import android.provider.MediaStore
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

//Create variables to hold the three strings
private var mFullName: String? = null
private var mAge: String? = null
private var mCity: String? = null
private var mCountry: String? = null
private var mHeight: String? = null
private var mWeight: String? = null
private var mSex: String? = null
private var mActivity_level: String? = null
private var mButtonSubmit: Button? = null
private var mButtonCamera: Button? = null


//Define a bitmap
private var mThumbnailImage: Bitmap? = null

//Define a global intent variable
private var mDisplayIntent: Intent? = null
class UserInfo : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)
        //Get the buttons
        mButtonSubmit = view.findViewById(R.id.button_submit) as Button
        mButtonCamera = view.findViewById<View>(R.id.button_pic) as Button
        //mFullName = view.findViewById<View>(R.id.tv_name) as TextView
       // mAge = view.findViewById<View>(R.id.tv_age) as TextView
        return inflater.inflate(R.layout.fragment_user_info, container, false)
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
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserInfo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}