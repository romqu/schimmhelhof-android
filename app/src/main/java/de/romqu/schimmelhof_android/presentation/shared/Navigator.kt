package de.romqu.schimmelhof_android.presentation.shared

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.presentation.login.LoginFragmentDirections
import de.romqu.schimmelhof_android.presentation.main.MainActivity
import de.romqu.schimmelhof_android.presentation.ridinglessonlist.ShowRidingLessonsFragmentDirections
import de.romqu.schimmelhof_android.presentation.shared.NavigatorDestination.Login
import de.romqu.schimmelhof_android.presentation.shared.NavigatorDestination.RidingLessons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@ActivityScoped
class Navigator @Inject constructor(
    navController: Provider<NavController>,
    @Named(NAVIGATION) stream: MutableSharedFlow<NavigatorDestination>,
    @Named("main") scope: CoroutineScope,
) {

    init {
        startCollecting(scope, stream, navController)
    }

    private fun startCollecting(
        scope: CoroutineScope,
        stream: MutableSharedFlow<NavigatorDestination>,
        navController: Provider<NavController>,
    ) {
        scope.launch {
            stream.collect { destination: NavigatorDestination ->
                navigateToDestination(destination, navController)
            }
        }
    }

    private fun navigateToDestination(
        destination: NavigatorDestination,
        navController: Provider<NavController>,
    ) {
        when (destination) {
            RidingLessons -> navController.get()
                .navigate(LoginFragmentDirections.actionLoginToRiding())
            Login -> navController.get()
                .navigate(ShowRidingLessonsFragmentDirections.actionRidingToLogin())
        }
    }
}

sealed class NavigatorDestination {
    object RidingLessons : NavigatorDestination()
    object Login : NavigatorDestination()
}

const val NAVIGATION = "NAVIGATION"

@Module
@InstallIn(SingletonComponent::class)
object NavigatorModule {

    @Provides
    @Singleton
    @Named(NAVIGATION)
    fun provideNavigatorStream() = MutableSharedFlow<NavigatorDestination>(
        extraBufferCapacity = Int.MAX_VALUE
    )
}

@Module
@InstallIn(ActivityComponent::class)
object NavControllerModule {

    @ExperimentalCoroutinesApi
    @Provides
    @ActivityScoped
    fun provideNavController(@ActivityContext context: Context): NavController =
        (context as MainActivity).supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)!!
            .findNavController()
}

