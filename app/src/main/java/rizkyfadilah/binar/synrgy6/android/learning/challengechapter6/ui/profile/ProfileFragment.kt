package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.beran.domain.model.UserData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.FragmentProfileBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.UserState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.showAlert
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.showToast
import java.util.Calendar

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private lateinit var username: String
    private lateinit var name: String
    private lateinit var birthday: String
    private lateinit var address: String
    private val viewModel by viewModels<SharedAuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserState.Loading -> showLoading(true)
                is UserState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                }

                is UserState.Success -> {
                    val user = state.userData
                    username = user.username
                    binding.tieUsername.setText(user.username)
                    name = user.name
                    binding.tieName.setText(user.name)
                    birthday = user.birthDay
                    binding.tieBirthday.setText(user.birthDay)
                    address = user.address
                    binding.tieAddress.setText(user.address)
                }
            }
        }

        binding.tieBirthday.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                    binding.tieBirthday.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePicker.show()
        }

        binding.btnLogout.setOnClickListener {
            requireContext().showAlert(
                title = getString(R.string.logout),
                message = getString(R.string.confirm_logout),
                positiveCallback = {
                    lifecycleScope.launch {
//                        viewModel.logOut()
                        findNavController().popBackStack(R.id.homeFragment, true)
                        findNavController().navigate(R.id.loginFragment)
                    }
                })
        }

        binding.btnUpdate.setOnClickListener {
            val newUsername = binding.tieUsername.text.toString().trim()
            val newName = binding.tieName.text.toString().trim()
            val newBirthday = binding.tieBirthday.text.toString().trim()
            val newAddress = binding.tieAddress.text.toString().trim()

            when {
                username != newUsername && name != newName && birthday != newBirthday && address != newAddress -> {
                    val user = UserData(
                        name = name,
                        username = username,
                        birthDay = birthday,
                        address = address,
                        imageUrl = "image.jpg"
                    )
                    viewModel.saveData(user)
                }

                else -> requireContext().showToast(getString(R.string.no_update))
            }
            binding.tieUsername.clearFocus()
            binding.tieName.clearFocus()
            binding.tieBirthday.clearFocus()
            binding.tieAddress.clearFocus()
        }

    }

    private fun showLoading(isLoading: Boolean) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}