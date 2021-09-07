package de.romqu.schimmelhof_android.presentation.ridinglessonlist.logout

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LogoutRunner @AssistedInject constructor(
    @Assisted
    private val scope: CoroutineScope,
) {
    fun onLogoutClick() {

    }

}

@AssistedFactory
interface LogoutRunnerFactory {
    fun create(scope: CoroutineScope): LogoutRunner
}