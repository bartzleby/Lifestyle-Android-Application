package com.lifestyle

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharedViewState(
    val gender: Int? = null,
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val activity: Int? = null
) : Parcelable

class SharedViewModel(private val savedState: SavedStateHandle) : ViewModel() {
    val state = savedState.getLiveData("state", SharedViewState())

    // Repetition is ugly, but the alternative (reflection) is even uglier ¯\_(ツ)_/¯
    // https://stackoverflow.com/questions/70498362/specify-field-to-copy-in-data-classes-kotlin
    // Also see https://stackoverflow.com/questions/61166786/how-to-save-livedata-into-savestatehandle for SavedStateHandle info

    fun selectGender(index: Int) {
        savedState["state"] = state.value?.copy(gender = index)
    }

    fun selectAge(value: Int) {
        savedState["state"] = state.value?.copy(age = value)
    }

    fun selectHeight(value: Int) {
        savedState["state"] = state.value?.copy(height = value)
    }

    fun selectWeight(value: Int) {
        savedState["state"] = state.value?.copy(weight = value)
    }

    fun selectActivity(index: Int) {
        savedState["state"] = state.value?.copy(activity = index)
    }
}
