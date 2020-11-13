package de.romqu.schimmelhof_android.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.romqu.schimmelhof_android.R
import de.romqu.schimmelhof_android.databinding.FragmentLoginBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.isLoginButtonEnabled.collect { isEnabled ->
                binding.loginBtn.isEnabled = isEnabled
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessage
                .collect {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                }
        }

        lifecycleScope.launch {
            viewModel.closeKeyboard
                .collect {
                    binding.root.hideKeyboard()
                }
        }

        lifecycleScope.launch {
            viewModel.goToRidingLessons.collect {
                findNavController().navigate(R.id.ridingLessonsFragment)
            }
        }

        binding.usernameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onUsernameTextChange(text.toString())
        }

        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onPlainPasswordTextChange(text.toString())
        }

        binding.usernameEditText.setText("14394")
        binding.passwordEditText.setText("J6WVh6ZHv7msMfMZLLWCSHzJMC6wkZeuqRWNis2WZBnhmvx5eskTN92")

        binding.loginBtn.setOnClickListener {
            viewModel.onLoginClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroy()
        _binding = null
    }

    private fun View.hideKeyboard() {
        ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
    }
}