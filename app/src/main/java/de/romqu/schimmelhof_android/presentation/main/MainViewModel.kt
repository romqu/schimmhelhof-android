package de.romqu.schimmelhof_android.presentation.main

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.data.ApiAuthData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    apiAuthDataStore: DataStore<ApiAuthData>,
) : ViewModel() {

    val initialDestination = apiAuthDataStore.data
        .map { it.bearerToken }
        .map { bearerToken ->
            if (bearerToken.isBlank()) {
                R.id.loginFragment
            } else {
                R.id.ridinglessonsFragment
            }
        }.take(1)
}