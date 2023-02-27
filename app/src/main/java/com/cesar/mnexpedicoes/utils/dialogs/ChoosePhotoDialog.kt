package com.cesar.mnexpedicoes.utils.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cesar.mnexpedicoes.databinding.DialogInsertPhotoBinding

class ChoosePhotoDialog(
    cameraCallback: () -> Unit,
    galleryCallback: () -> Unit
) : DialogFragment() {

    private var _binding: DialogInsertPhotoBinding? = null
    private val binding: DialogInsertPhotoBinding
        get() = requireNotNull(_binding)
    private val camCallback = cameraCallback
    private val galCallback = galleryCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogInsertPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        cameraBtn(camCallback)
        galleryBtn(galCallback)
    }

    private fun cameraBtn(callback: () -> Unit) {
        binding.cdvCamera.setOnClickListener {
            callback()
            this.dismiss()
        }
    }

    private fun galleryBtn(callback: () -> Unit) {
        binding.cdvGallery.setOnClickListener {
            callback()
            this.dismiss()
        }
    }
}