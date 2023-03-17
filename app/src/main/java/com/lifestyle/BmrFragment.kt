package com.lifestyle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifestyle.databinding.FragmentBmrBinding

enum class Gender {
    MALE, FEMALE
}

enum class Activity(val multiplier: Float) {
    SEDENTARY(1.2f),
    LIGHT(1.375f),
    MODERATE(1.55f),
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

class BmrFragment : Fragment() {
    private var _binding: FragmentBmrBinding? = null

    private val binding get() = _binding!!
    // TODO: Source these values from the registration segment; pass via bundle, maybe use the text view values directly?
    private val gender: Gender = Gender.MALE
    private val age: Int = 26
    private val height: Int = 175
    private val weight: Int = 61
    private val activity: Activity = Activity.LIGHT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calculatedBMR: Int = calculateBMR(gender, age, height, weight)
        binding.textViewBmrBasePreface.text = getString(R.string.text_view_bmr_base_preface).format(
            gender,
            age,
            height,
            weight
        )
        binding.textViewBmrTargetPreface.text = getString(R.string.text_view_bmr_target_preface).format(activity)
        binding.textViewBmrBaseCalculated.text = getString(R.string.text_view_bmr_base_calculated).format(calculatedBMR)
        binding.textViewBmrTargetCalculated.text = getString(R.string.text_view_bmr_target_calculated).format(
            (calculatedBMR * activity.multiplier).toInt()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
