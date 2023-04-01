package com.lifestyle

import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized

class LifestyleRepository private constructor(lifestyleDao: LifestyleDao) {
    // This LiveData object that is notified when we've fetched the weather
    val userDataLive = MutableLiveData<UserData>()

    private var mUserData: UserData? = null
    private var mLifestyleDao: LifestyleDao = lifestyleDao

    fun setUserData(userData: UserData) {
        // First cache the data
        mUserData = userData

        // Everything within the scope happens logically sequentially
        mScope.launch(Dispatchers.IO) {
            // Populate live data object. But since this is happening in a background thread (the coroutine),
            // we have to use postValue rather than setValue. Use setValue if update is on main thread
            userDataLive.postValue(userData)

            insert()
        }
    }

    @WorkerThread
    suspend fun insert() {
        if (mUserData != null) {
            mLifestyleDao.insert(UserData(mUserData!!.fullName!!))
        }
    }

    // Make the repository singleton. Could in theory
    // make this an object class, but the companion object approach
    // is nicer (imo)
    companion object {
        private var mInstance: LifestyleRepository? = null
        private lateinit var mScope: CoroutineScope

        @Synchronized
        fun getInstance(
            lifestyleDao: LifestyleDao,
            scope: CoroutineScope
        ): LifestyleRepository {
            mScope = scope
            return mInstance ?: synchronized(this) {
                val instance = LifestyleRepository(lifestyleDao)
                mInstance = instance
                instance
            }
        }
    }
}