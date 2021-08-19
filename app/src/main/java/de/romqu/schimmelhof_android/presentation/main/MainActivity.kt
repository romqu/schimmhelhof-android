package de.romqu.schimmelhof_android.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.presentation.shared.Navigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigator: Navigator

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