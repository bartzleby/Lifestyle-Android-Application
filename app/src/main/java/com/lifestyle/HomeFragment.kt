package com.lifestyle

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import com.lifestyle.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private var mButtonRegister: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

       // mButtonRegister = findViewById<View>(R.id.button_register) as Button
        //mButtonRegister!!.setOnClickListener(this)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            // TODO: Add registration fragment (for now, just collects user data for local use throughout the app)


            com.lifestyle.R.id.UserInfo?.let {
                activity!!.supportFragmentManager.commit {
                    activity!!.findNavController(com.lifestyle.R.id.nav_host_fragment_content_main)
                        .navigate(com.lifestyle.R.id.UserInfo)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
