package de.romqu.schimmelhof_android.presentation.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.data.network.NetworkModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityEmitter: MutableSharedFlow<NetworkModule.NetworkConnectivityState>

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Schimmelhofandroid)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }

    private fun setup() {
        lifecycleScope.launchWhenStarted {
            setupInitialDestination()
        }

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityEmitter.tryEmit(NetworkModule.NetworkConnectivityState.CONNECTED)
            }

            override fun onLost(network: Network) {
                connectivityEmitter.tryEmit(NetworkModule.NetworkConnectivityState.DISCONNECTED)
            }
        })
    }

    private suspend fun setupInitialDestination() {
        viewModel.initialDestination.collect {

            val navInflater = navController.navInflater
            val graph = navInflater.inflate(R.navigation.nav_graph)

            graph.startDestination = it

            navController.graph = graph
        }
    }
}