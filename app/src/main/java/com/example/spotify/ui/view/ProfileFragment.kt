package com.example.spotify.ui.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.spotify.R
import com.example.spotify.ui.view.SplashScreenActivity.Companion.prefs
import com.example.spotify.databinding.FragmentProfileBinding
import com.example.spotify.util.Constant.REQUEST_CAMERA
import com.example.spotify.util.Constant.REQUEST_GALLERY
import com.example.spotify.ui.viewmodel.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var photo: Uri? = null
    private val viewModel: ProfileViewModel by viewModels()


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        showPhoto()
        setup()
        bottomSheetDialog()
        return binding.root
    }

    private fun setup() {
        binding.btBack.setOnClickListener {
            prefs.wipeNameUser()
            prefs.wipePhotoUser()
            prefs.wipeUser()
            onBackPressed()
        }
        binding.tvGmail.text = prefs.getNameUser()
    }

    private fun onBackPressed() {
        startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun bottomSheetDialog() {

        binding.ivCamera.setOnClickListener {
            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            val close = view.findViewById<ImageView>(R.id.ivExit)
            close.setOnClickListener { dialog?.dismiss() }
            dialog?.setCancelable(true)
            dialog?.setContentView(view)
            dialog?.show()
            val gallery = view.findViewById<ImageView>(R.id.ivGallery)
            gallery.setOnClickListener { validatePermissionToOpenGallery() }
            val camera = view.findViewById<ImageView>(R.id.ivCamera)
            camera.setOnClickListener { validatePermissionToOpenCamera() }
        }
    }

    private fun validatePermissionToOpenCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context?.let { checkSelfPermission(it, Manifest.permission.CAMERA) }
                == PackageManager.PERMISSION_DENIED
                || context?.let {
                    checkSelfPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
                == PackageManager.PERMISSION_DENIED) {
                // pedir permiso al usuario
                val cameraPermission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(cameraPermission, REQUEST_CAMERA)
            } else
                openCamera()
        } else
            openCamera()
    }

    private fun validatePermissionToOpenGallery() {
        // Verificar la version si es marshmallow en adelante 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context?.let {
                    checkSelfPermission(
                        it,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } == PackageManager.PERMISSION_DENIED) {
                val galleryPermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(galleryPermission, REQUEST_GALLERY)
            } else
                openGallery()
        } else
            openGallery()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openGallery()
                else
                    Toast.makeText(context, "No se puede acceder a tu galeria", Toast.LENGTH_SHORT)
                        .show()
            }
            REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openCamera()
                else
                    Toast.makeText(context, "No se puede acceder a tu Camara", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    private fun openCamera() {
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "Nueva imagen")
        photo = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            value
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }

    private fun openGallery() {
        val intentGallery = Intent(Intent.ACTION_PICK)
        intentGallery.type = "image/*"
        startActivityForResult(intentGallery, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) -> {
                viewModel.savePhoto(data?.data)
                showPhoto()
            }
            (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA) -> {
                viewModel.savePhoto(photo)
                showPhoto()
            }
        }
    }

    private fun showPhoto(){
        if (prefs.getPhotoUser() == ""){
            Glide.with(this)
                .load("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBw0NDQ0ODQ0NDQ0NDQ0PDg0NDQ8NDQ4NFREWFhcRFhYYHSggGBonGxUVITIhJSkrLi4uFx8zOD8sNygtLisBCgoKDg0OFxAQGC0dHR0uLSstKystKysrKy0tLS0tLS0tKystLS0rLSstLTItLS0rKys3Ky03LS03LSsrKysrK//AABEIAOAA4QMBIgACEQEDEQH/xAAbAAEBAQEBAQEBAAAAAAAAAAAAAQUGBAMCB//EADkQAQACAQEFBAULAwUAAAAAAAABAgMRBAUhMUESUWFxQnKRobETIiMyM1Jic4HB4RWS0VOCssLx/8QAGAEBAQEBAQAAAAAAAAAAAAAAAAIDAQT/xAAdEQEBAQACAwEBAAAAAAAAAAAAAQIRMQMSQSFR/9oADAMBAAIRAxEAPwD+iAPSyAAAAAAAAAARQAFrWZnSImZ7ojWXopu/NbljmPOYj4ucyOvMPb/Ss2mulf7niJZTgBHXFAAAAAAAAAAAAAAAAAAAAfvDhvedKVm0+HKPNobDuubaWy6xHSnKZ8+5r48daxpWIiI6RGjO746VMsnBue08clojwrxn2vdi3dhr6Hanvt87+HrEXVquIlaxHKIjyjRQS6xt95rxMU4xSY19ae5luqy4q3iYtEWieksTb93Tj1tTW1OvfX+GuNTpGo8ADRIAAAAAAAAACAAqKgKioAKAjX3RsXLLePUj/szdmxdu9KfetET5dfc6isREREcIiNIjwZ7vxWYoDJYAAAAADE3psHY+kpHzfSrHo+MeDMdbaImJieMTzjvhzO2YPkslq9OdfVlrjXP4jUfABokAAAAAAVAAABUAVBQQAHs3T9vT/d/xl0Tnt0R9PXwi3wdCx32vPQAhQAAAAAAwt+R9LHqR8ZbrD359pX8uPjK8dua6ZoDZmAAAAAAAoIAAoAgKCCgPbuaPpo9W3wdAwNzfbR6tv2b7Hfa89ACFAAAAAADD379rX8uPjLcYe/fta/lx8ZXjtzXTNAbMwAAAAAAFBAAUAAAAAHu3NE/LROnDs249OjfeXdk64cfl79Zephq81pABLoAAAAAAw9+/a1/Lj4y3Hk3lWvyOSZiJns8J04668FZvFcrnAVuzBFARQEFQDRQBAUAAAAAAHQbntrgr4TaPfr+72sncWbhenXXtR5cp/ZrMNdtJ0AJdAAAAAAHj3vbTBfx7Me+HsZe/cula06zbX9I/9dz25emMIPQzAABUBUABQBAUAAAAAAH7wZZpetonTSYnh3dYdTE6uSdNsGTtYsc/hiP1jh+zLyT6vL0AM1AAAAAADl9qzTkva0zrrM6eFekOg3hl7GK89dNI854OZaeOfU6BUaoBQEFAQFAABFQBQAAAAARq7k2mImcc9Z1r59YZZEzExMcJjjExziXLOY7Lw60fDYclr4qWtzmOPt5vu87QAAAAB+MttK2mOlZn3AyN97RrNccejxt59IZazaZmZmdZmdZnvlHozOIzqiK64IAAAKgAoAIqKAAAAAAAC1pa3CsTafCJkHS7FGmLHH4K/B935x10rEd0RD9PM1AAAAHz2mdMd/Ut8H0fLao1x5IjjPYtpEeQOWFtWYnSYmJjpMaSj0slQAAAAAAUARQRUUAAEUAAAHRbrxRXDTvtHanxmWDs+Kcl60j0p08o6y6elYiIiOEREREeDPyX4rL9AMlgAAAAAMnfuONKX66zWfLn/ljum2/B8pjtXrzr60OZbYv4jQKLSgACoACgAgAACgAD6YNnvknSlZnx5RH6tPZtzxzy21/DXhHtTdSOycsmlZtOlYmZ7ojWXv2fdOS3G8xSO7nZs4sNKRpWsVjwh9Gd3fivV5tl2HHi41jW33pnWXpBCgAAAAAAAB4No3XivrMa0tP3eWvk947LwOe2jdmWnGI7cd9efseOYda+OfZceT69Ynx5T7Vzyf1Ny5cau0bnmOOO2v4bcJ9rOy4b0nS9ZrPjHNc1Kmx8wFOAAKACD6YcNsk9mkaz7o82xsm6aV0nJ8+e70Y/ym6kdk5ZWzbJky/Vrw+9PCvta2zbppXjee3Pdyr7OrQiNFZ3dq5lK1iI0iNIjpHCFBDoAAAAAAAAAAAAAAAA/N6RaNLRExPSY1h+gGZtO6KW4457E908a/wy9o2XJi+vXh96ONfa6dJjVU3Ym5ckN3at1UvrNPmW7vRn9OjH2jZ7450vGndPOJ8ms1Kmzh8lRVOOn2bZ64qxWsec9Znvl9geZqAAAAAAAAAAAAAAAAAAAAAAAAPltOCuSs1tHDpPWJ74fUBif0a3+pH9sjbFe9c9Y//Z")
                .into(binding.ivProfile)
        }else {
            context?.let { viewModel.getPhotoForShow(it, binding.ivProfile)
            viewModel.progress.observe(viewLifecycleOwner,{ visible ->
                binding.pbLoad.visibility = if(visible) View.VISIBLE else View.GONE
            })
            }
        }
    }


}


