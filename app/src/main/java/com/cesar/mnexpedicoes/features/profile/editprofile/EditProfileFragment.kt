package com.cesar.mnexpedicoes.features.profile.editprofile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.FragmentEditProfileBinding
import com.cesar.mnexpedicoes.features.login.register.model.UserResponse
import com.cesar.mnexpedicoes.features.profile.editprofile.model.EditProfileUserResponse
import com.cesar.mnexpedicoes.features.profile.editprofile.validation.InputsValidation
import com.cesar.mnexpedicoes.features.profile.presentation.ProfileFragment
import com.cesar.mnexpedicoes.utils.*
import com.cesar.mnexpedicoes.utils.dialogs.ChoosePhotoDialog
import java.util.*

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding: FragmentEditProfileBinding
        get() = requireNotNull(_binding)
    private val viewModel: EditProfileFragmentViewModel by viewModels()

    private val inputsValidation = InputsValidation()
    private var user: UserResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getSerializable("user") as? UserResponse ?: UserResponse()
        binding.cdvNext.toggleAlpha(false)
        inputsValidation.setup(binding)
        setupProfile()
    }

    private fun setupProfile() {
        user?.let {
            if (it.name != "") {
                binding.tieName.setText(it.name)
            }
            if (it.phone != "") {
                binding.tiePhone.setText(it.phone)
            }
            if (it.birthday != "") {
                binding.tieBirthday.setText(it.birthday)
            }
            if (it.cpf != "") {
                binding.tieCpf.setText(it.cpf)
            }
        }

        binding.cdvNext.setOnClickListener {
            if (inputsValidation.inputsAreValid) {
                setProgress()
                updateUserByPhone()
            }
        }

        setupChoosePhotoDialog()
        val profileDrawable = getUserProfilePhoto(user?.phone.toString())
        if (profileDrawable != null) {
            binding.imgProfile.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.imgProfile.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.imgProfile.setImageDrawable(profileDrawable)
        } else {
            binding.imgProfile.setImageResource(R.drawable.ic_add_photo_black_42)
        }

    }

    private fun updateUserByPhone() {
        val editProfileUserResponse = EditProfileUserResponse(
            phone = binding.tiePhone.unMasked,
            name = binding.tieName.text.toString(),
            birthday = binding.tieBirthday.text.toString().formatToDateEnUs(),
            cpf = binding.tieCpf.unMasked
        )
        viewModel.updateUserByPhone(editProfileUserResponse) {
            setProgress()
            if (viewModel.userUpdatedSuccessfull) {
                val sharedPref = getSharedPreferences()
                sharedPref?.edit()?.putString("phone", "")?.apply()
                makeToast("Conta alterada com sucesso!")
                push(ProfileFragment())
            } else {
                makeToast(getString(R.string.generic_error))
            }
        }
    }

    private fun setProgress() {
        binding.imgNext.toggleVisibility()
        binding.pgbLoading.toggleVisibility()
    }

    private fun setupChoosePhotoDialog() {
        binding.cdvProfileImg.setOnClickListener {
            val dialog = ChoosePhotoDialog(
                { openCamera() },
                { openGallery() }
            )
            dialog.show(parentFragmentManager, "ChoosePhotoDialog")
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.REQUEST_CODE_GALLERY)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA)
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == Constants.REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && intent != null) {
            binding.imgProfile.setImageURI(intent.data)
            saveProfileImage(imgUri = intent.data, userId = user?.phone.toString())
        }
        if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && intent != null) {
            val bitmap = intent.extras?.get("data") as Bitmap
            binding.imgProfile.setImageBitmap(bitmap)
            saveProfileImage(imgBitmap = bitmap, userId = user?.phone.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).apply {
            bottomBarHidden = true
            backBtnVisible = true
        }
    }
}