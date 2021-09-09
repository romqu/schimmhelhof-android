package de.romqu.schimmelhof_android.presentation.ridinglessonlist.logout

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import de.romqu.schimmelhof_android.domain.LogoutService
import de.romqu.schimmelhof_android.presentation.shared.NAVIGATION
import de.romqu.schimmelhof_android.presentation.shared.NavigatorDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Named

@ExperimentalCoroutinesApi
class LogoutRunner @AssistedInject constructor(
    @Assisted
    private val scope: CoroutineScope,
    private val logoutService: LogoutService,
    @Named(NAVIGATION)
    private val navigation: MutableSharedFlow<NavigatorDestination>,
) {
    fun onLogoutClick() {
        scope.launch {
            logoutService.execute()
                .doOn({ navigation.emit(NavigatorDestination.Login) }, {})
        }
    }

}

@AssistedFactory
interface LogoutRunnerFactory {
    fun create(scope: CoroutineScope): LogoutRunner
}