package de.romqu.schimmelhof_android.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.romqu.schimmelhof_android.domain.LoginService
import de.romqu.schimmelhof_android.presentation.shared.NavigatorDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService,
    private val savedStateHandle: SavedStateHandle,
    @Named("q")
    private val stream: MutableSharedFlow<NavigatorDestination>,
) : ViewModel() {

    companion object {
        const val USERNAME_KEY = "USERNAME_KEY"
        const val PASSWORD_KEY = "PASSWORD_KEY"
    }

    private val usernameTextState = MutableStateFlow(savedStateHandle.get(USERNAME_KEY) ?: "")
    private val plainPasswordTextState = MutableStateFlow(savedStateHandle.get(PASSWORD_KEY) ?: "")

    val errorMessage = MutableSharedFlow<String>()
    val closeKeyboard = MutableSharedFlow<Unit>()

    val isLoginButtonEnabled = usernameTextState
        .combine(plainPasswordTextState) { usernameText, plainPasswordText ->
            usernameText.isNotBlank() && plainPasswordText.isNotBlank()
        }

    fun onUsernameTextChange(usernameText: String) {
        usernameTextState.value = usernameText
    }

    fun onPlainPasswordTextChange(passwordText: String) {
        plainPasswordTextState.value = passwordText
    }

    fun onLoginClick() {
        viewModelScope.launch {
            closeKeyboard()
            doLogin()
        }
    }

    private suspend fun closeKeyboard() {
        closeKeyboard.emit(Unit)
    }

    private suspend fun doLogin() {
        loginService.execute(
            username = usernameTextState.value,
            plainPassword = plainPasswordTextState.value
        ).doOn({
            stream.tryEmit(NavigatorDestination.RidingLessons)
        }, {
            errorMessage.emit("Da stimmen wohl die Daten nich, wa?")
        })
    }

    fun onDestroy() {
        savedStateHandle.set(PASSWORD_KEY, usernameTextState.value)
        savedStateHandle.set(USERNAME_KEY, plainPasswordTextState.value)
    }
}