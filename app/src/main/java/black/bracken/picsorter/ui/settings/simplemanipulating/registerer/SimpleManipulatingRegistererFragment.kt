package black.bracken.picsorter.ui.settings.simplemanipulating.registerer

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import black.bracken.picsorter.R
import black.bracken.picsorter.databinding.SimpleManipulatingRegistererFragmentBinding
import black.bracken.picsorter.ext.setOnTextChanged
import black.bracken.picsorter.ui.settings.simplemanipulating.registerer.SimpleManipulatingRegistererViewModel.VerificationResult
import black.bracken.picsorter.util.hasExternalStoragePermission
import black.bracken.picsorter.util.openDialogForExternalStoragePermission
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import kotlinx.android.synthetic.main.simple_manipulating_registerer_fragment.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SimpleManipulatingRegistererFragment : Fragment() {

    private val viewModel by viewModel<SimpleManipulatingRegistererViewModel>()

    private val requestPermissionToOpenDirChooserIntent =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isAllowed ->
            if (isAllowed) {
                showDialogToChooseDirectory()
            } else if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                openDialogForExternalStoragePermission(this)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SimpleManipulatingRegistererFragmentBinding>(
            inflater, R.layout.simple_manipulating_registerer_fragment, container, false
        ).also { binding ->
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbarSimpleManipulatingRegisterer)
        toolbarSimpleManipulatingRegisterer.setTitle(R.string.title_simple_manipulating_registerer)

        buttonRegister.setOnClickListener { onPressedRegisterButton() }
        buttonChangeDirectory.setOnClickListener {
            if (hasExternalStoragePermission()) {
                showDialogToChooseDirectory()
            } else {
                requestPermissionToOpenDirChooserIntent.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        editDelaySeconds.setOnTextChanged { secondsText ->
            viewModel.secondsToDelete.value = secondsText.toIntOrNull()
        }
        switchToDeleteLater.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.shouldDeleteLater.value = isEnabled
            editDelaySeconds.isEnabled = isEnabled
        }

        viewModel.directoryPath.observe(viewLifecycleOwner) { textDirectoryPath.text = it }
    }

    private fun onPressedRegisterButton() = viewModel.viewModelScope.launch {
        when (viewModel.register()) {
            VerificationResult.SUCCEED -> {
                setOf(editManipulatingName, editDelaySeconds).forEach(::closeSoftKeyboard)
                findNavController().navigate(R.id.action_simpleManipulatingRegistererFragment_to_simpleManipulatingSettingsFragment)
            }
            VerificationResult.MUST_NOT_EMPTY -> {
                Toast.makeText(
                    context ?: return@launch,
                    R.string.error_must_not_empty,
                    Toast.LENGTH_SHORT
                ).show()
            }
            VerificationResult.ALREADY_REGISTERED_UNDER_SAME_NAME -> {
                Toast.makeText(
                    context ?: return@launch,
                    R.string.error_duplication,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showDialogToChooseDirectory() {
        MaterialDialog(context ?: return).show {
            folderChooser(
                context,
                initialDirectory = File("/storage/emulated/0/"),
                allowFolderCreation = true
            ) { _, file ->
                viewModel.directoryPath.value = file.absolutePath
            }
        }
    }

    private fun closeSoftKeyboard(view: View) {
        (activity?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
