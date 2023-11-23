package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.beran.data.local.pref.SessionManager
import com.beran.data.utils.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.FragmentLoginBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.AuthState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.profile.SharedAuthViewModel
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.showToast

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SharedAuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sessionState.observe(viewLifecycleOwner){state ->
            when(state){
                is AuthState.Loading -> {
                    binding.btnLogin.isEnabled = false
                }
                is AuthState.Error -> {
                    requireContext().showToast(getString(R.string.login_failed_try_again))
                }
                is AuthState.Success -> {
                    val navOption = NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, inclusive = true)
                        .build()
                    findNavController().navigate(R.id.homeFragment, null, navOption)
                    requireContext().showToast(getString(R.string.success_login))
                }
            }
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                binding.etPassword.error =
                    if (text.length < 6) getString(R.string.error_password_length) else null
            } else {
                binding.etPassword.error = null
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() && password.isEmpty()) {
                requireContext().showToast(getString(R.string.error_empty_field))
            } else {
                lifecycleScope.launch {
                    viewModel.createSession()
                }
            }
        }

        binding.tvNavigateToRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            val isLogin =
                requireContext().dataStore.data.map { it[SessionManager.KEY_LOGIN] ?: false }
                    .first()
            Log.d("LoginFragment", "isLogin: $isLogin")
            if (isLogin) {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
                findNavController().navigate(R.id.homeFragment, null, navOptions)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}