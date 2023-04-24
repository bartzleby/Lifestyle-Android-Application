package com.lifestyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lifestyle.databinding.FragmentBmrBinding

enum class Gender {
    MALE, FEMALE
}

enum class Activity(val multiplier: Float) {
    SEDENTARY(1.2f),
    LIGHTLY_ACTIVE(1.375f),
    MODERATELY_ACTIVE(1.55f),
    VERY_ACTIVE(1.725f),
    EXTRA_ACTIVE(1.9f)
}

fun calculateBMR(gender: Gender, age: Int, height: Int, weight: Int): Int {
    // Mifflin-St Jeor equation (expects metric units)
    // https://pubmed.ncbi.nlm.nih.gov/2305711/
    val calculatedBMR: Int = (10 * weight + 6.25 * height - 5 * age).toInt()
    return when (gender) {
        Gender.MALE -> calculatedBMR + 5
        Gender.FEMALE -> calculatedBMR - 161
    }
}

fun inchesToCentimeters(inches: Int): Int {
    return (inches * 2.54).toInt()
}

fun poundsToKilograms(pounds: Int): Int {
    return (pounds / 2.205).toInt()
}

class BmrFragment : Fragment() {
    private var _binding: FragmentBmrBinding? = null

    private val binding get() = _binding!!

    private val mLifestyleViewModel: LifestyleViewModel by viewModels {
        LifestyleViewModelFactory((requireContext().applicationContext as LifestyleApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmrBinding.inflate(inflater, container, false)

        mLifestyleViewModel.liveUserData.observe(this, userDataObserver)

        return binding.root
    }

    private val userDataObserver: Observer<UserData> = Observer { userData ->
        userData?.let {
            val gender = Gender.valueOf(userData.sex!!.uppercase())
            val age = userData.age!!
            val height = userData.height!!
            val weight = userData.weight!!
            val activity = Activity.valueOf(userData.activity!!.replace(" ", "_").uppercase())
            val calculatedBMR: Int = calculateBMR(gender, age, inchesToCentimeters(height), poundsToKilograms(weight))
            binding.textViewBmrBasePreface.text = getString(R.string.text_view_bmr_base_preface).format(
                gender,
                age,
                "%d'%d\"".format(height / 12, height % 12),
                weight
            )
            binding.textViewBmrTargetPreface.text = getString(R.string.text_view_bmr_target_preface).format(activity)
            binding.textViewBmrBaseCalculated.text = getString(R.string.text_view_bmr_base_calculated).format(calculatedBMR)
            binding.textViewBmrTargetCalculated.text = getString(R.string.text_view_bmr_target_calculated).format(
                (calculatedBMR * activity.multiplier).toInt()
            )
        } ?: run {
            binding.textViewBmrBasePreface.text = resources.getString(R.string.text_view_bmr_awaiting_user_data)
            binding.textViewBmrTargetPreface.text = ""
            binding.textViewBmrBaseCalculated.text = ""
            binding.textViewBmrTargetCalculated.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
