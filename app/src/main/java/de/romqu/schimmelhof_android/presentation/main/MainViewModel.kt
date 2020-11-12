package de.romqu.schimmelhof_android.presentation.main

import androidx.datastore.DataStore
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.data.ApiAuthData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val apiAuthDataStore: DataStore<ApiAuthData>,
) : ViewModel() {

    val initialDestination = apiAuthDataStore.data
        .map { it.bearerToken }
        .map { bearerToken ->
            if (bearerToken.isBlank()) {
                R.id.loginFragment
            } else {
                R.id.ridingLessonsFragment
            }
        }.take(1)
}