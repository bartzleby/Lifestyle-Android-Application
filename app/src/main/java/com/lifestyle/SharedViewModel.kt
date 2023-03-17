package com.lifestyle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class SharedViewState(
    val gender: Int? = null,
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val activity: Int? = null
)

class SharedViewModel : ViewModel() {
    private val mutableState = MutableLiveData(SharedViewState())
    val state: LiveData<SharedViewState> get() = mutableState

    // Repetition is ugly, but the alternative (reflection) is even uglier ¯\_(ツ)_/¯
    // https://stackoverflow.com/questions/70498362/specify-field-to-copy-in-data-classes-kotlin

    fun selectGender(index: Int) {
        mutableState.value = state.value?.copy(gender = index)
    }

    fun selectAge(value: Int) {
        mutableState.value = state.value?.copy(age = value)
    }

    fun selectHeight(value: Int) {
        mutableState.value = state.value?.copy(height = value)
    }

    fun selectWeight(value: Int) {
        mutableState.value = state.value?.copy(weight = value)
    }

    fun selectActivity(index: Int) {
        mutableState.value = state.value?.copy(activity = index)
    }
}
