package com.lifestyle

import androidx.lifecycle.*

class LifestyleViewModel(repository: LifestyleRepository) : ViewModel() {
    // Connect a live data object to the user data
    private val userData: LiveData<UserData> = repository.userDataLive

    //The singleton repository. If our app maps to one process, the recommended
    // pattern is to make repo and db singletons. That said, it's sometimes useful
    // to have more than one repo so it doesn't become a kitchen sink class, but each
    // of those repos could be singleton.
    private var mLifestyleRepository: LifestyleRepository = repository

    fun setUserData(userData: UserData) {
        // Simply pass the user's data to the repository
        mLifestyleRepository.setUserData(userData)
    }

    // Returns the data contained in the live data object
    val data: LiveData<UserData>
        get() = userData
}

// This factory class allows us to define custom constructors for the view model
class LifestyleViewModelFactory(private val repository: LifestyleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LifestyleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LifestyleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
