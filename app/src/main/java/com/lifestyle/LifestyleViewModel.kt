package com.lifestyle

import androidx.lifecycle.*

class LifestyleViewModel(repository: LifestyleRepository) : ViewModel() {

    val liveUserData: LiveData<UserData> = repository.currentUserData.asLiveData()
    val currentWeather: LiveData<WeatherDataCurrent> = repository.currentWeatherData.asLiveData()
    val forecastWeather: LiveData<List<WeatherDataForecast>> =
        repository.forecastWeatherData.asLiveData()

    // The singleton repository. If our app maps to one process, the recommended
    // pattern is to make repo and db singletons. That said, it's sometimes useful
    // to have more than one repo so it doesn't become a kitchen sink class, but each
    // of those repos could be singleton.
    private var mLifestyleRepository: LifestyleRepository = repository

    fun setUserData(userData: UserData) {
        mLifestyleRepository.setUserData(userData)
    }

    fun updateUserData(userData: UserData) {
        mLifestyleRepository.updateUserData(userData)
    }

    fun clearActive() {
        mLifestyleRepository.clearActive()
    }

    fun clearUserData() {
        mLifestyleRepository.clearUserData()
    }

    fun clearWeatherData() {
        mLifestyleRepository.clearWeatherData()
    }

    fun fetchWeather(loc: String) {
        return mLifestyleRepository.fetchWeather(loc)
    }
}

// This factory class allows us to define custom constructors for the view model
class LifestyleViewModelFactory(private val repository: LifestyleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LifestyleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LifestyleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
