package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.profile

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.FragmentCropPhotoBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class CropPhotoFragment : Fragment() {
    private var _binding: FragmentCropPhotoBinding? = null
    private val binding get() = _binding!!
    private val args: CropPhotoFragmentArgs by navArgs()
    private val viewModel: SharedAuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgUri = Uri.parse(args.img)

        binding.civPhoto.setImageUriAsync(imgUri)
        binding.btnFinish.setOnClickListener {
            val bitmap = binding.civPhoto.getCroppedImage(reqHeight = 400, reqWidth = 400)
            if (bitmap != null) {
                val uri = getImageUriFromBitmap(bitmap)
                viewModel.blurImage(uri)
                findNavController().navigateUp()
            }
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

    }


    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val tempFile = File.createTempFile("temprentpk", ".png")
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCropPhotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}