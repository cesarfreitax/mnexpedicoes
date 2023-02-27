package com.cesar.mnexpedicoes.features.profile.presentation

import android.app.Activity.RESULT_OK
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
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.FragmentProfileBinding
import com.cesar.mnexpedicoes.features.profile.editprofile.EditProfileFragment
import com.cesar.mnexpedicoes.features.profile.favorites.FavoritesFragment
import com.cesar.mnexpedicoes.features.profile.help.HelpFragment
import com.cesar.mnexpedicoes.features.profile.history.HistoryFragment
import com.cesar.mnexpedicoes.features.profile.tickets.TicketsFragment
import com.cesar.mnexpedicoes.utils.*
import com.cesar.mnexpedicoes.utils.dialogs.ChoosePhotoDialog

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = requireNotNull(_binding)

    private val viewModel: ProfileFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        getUser()
        setupNavigation()
    }

    private fun setupProfile() {
        setupProfilePhoto()
        setupChoosePhotoDialog()
    }

    private fun setupProfilePhoto() {
        val profileDrawable = getUserProfilePhoto(viewModel.user.phone.toString())
        if (profileDrawable != null) {
            binding.imgProfile.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.imgProfile.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.imgProfile.setImageDrawable(profileDrawable)
        } else {
            binding.imgProfile.setImageResource(R.drawable.ic_add_photo_white_24)
        }
    }

    private fun setupChoosePhotoDialog() {
        binding.cdvProfileImg.setOnClickListener {
            val dialog = ChoosePhotoDialog(
                {openCamera()},
                {openGallery()}
            )
            dialog.show(parentFragmentManager, "ChoosePhotoDialog")
        }
    }

    private fun setupNavigation() {
        binding.nvvProfile.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.edit_profile -> {
                    val args = Bundle()
                    args.putSerializable("user", viewModel.user)
                    push(EditProfileFragment(), args)
                }
                R.id.favorites -> {
                    push(FavoritesFragment())
                }
                R.id.history -> {
                    push(HistoryFragment())
                }
                R.id.tickets -> {
                    push(TicketsFragment())
                }
                R.id.help -> {
                    push(HelpFragment())
                }
                R.id.logout -> {
                    val sharedPref = getSharedPreferences()
                    sharedPref?.edit()?.putString("userId", "")?.apply()
                    sharedPref?.edit()?.putString("phone", "")?.apply()
                    navigateToActivity(LoginActivity())
                }
            }
            true
        }
    }

    private fun getUser() {
        val sharedPref = getSharedPreferences()
        val phone = sharedPref?.getString("userId", "")
        viewModel.getUser(phone.toString()) {
            binding.txtUserName.text = viewModel.user.name
            setupProfile()
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
        if (requestCode == Constants.REQUEST_CODE_GALLERY && resultCode == RESULT_OK && intent != null) {
            binding.imgProfile.setImageURI(intent.data)
            saveProfileImage(imgUri = intent.data, userId = viewModel.user.phone.toString())
        }
        if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == RESULT_OK && intent != null) {
            val bitmap = intent.extras?.get("data") as Bitmap
            binding.imgProfile.setImageBitmap(bitmap)
            saveProfileImage(imgBitmap = bitmap, userId = viewModel.user.phone.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.apply {
                bottomBarHidden = false
                backBtnVisible = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}