package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.beran.domain.model.UserData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.FragmentRegisterBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.AuthState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.profile.SharedAuthViewModel
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.showToast

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SharedAuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sessionState.observe(viewLifecycleOwner){state ->
            when(state){
                is AuthState.Loading -> {
                    binding.btnRegister.isEnabled = false
                }
                is AuthState.Error -> {
                    requireContext().showToast(getString(R.string.register_failed_try_again))
                }
                is AuthState.Success -> {
                    findNavController().popBackStack(R.id.loginFragment, true)
                    findNavController().navigate(R.id.homeFragment)
                    requireContext().showToast(getString(R.string.success_register))
                }
            }
        }

        binding.etPasswordConfirm.doAfterTextChanged {
            val password = binding.etPassword.text.toString().trim()
            val passwordConfirm = binding.etPasswordConfirm.text.toString().trim()
            binding.etPasswordConfirm.error =
                if (password != passwordConfirm) getString(R.string.error_password_not_match) else null
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                binding.etPassword.error =
                    if (text.length < 6) getString(R.string.error_password_length) else null
            } else {
                binding.etPassword.error = null
            }
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val passwordConfirm = binding.etPasswordConfirm.text.toString().trim()

            if (username.isEmpty() && email.isEmpty() && password.isEmpty() && passwordConfirm.isEmpty()) {
                requireContext().showToast(getString(R.string.error_empty_field))
            } else if (binding.etPasswordConfirm.error != null || binding.etPassword.error != null) {
                requireContext().showToast(getString(R.string.error_password))
            } else {
                lifecycleScope.launch {
                    val user = UserData(
                        name = "",
                        username = username,
                        birthDay = "",
                        address = "",
                        imageUrl = ""
                    )
                    viewModel.saveData(user)
                    viewModel.createSession()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}