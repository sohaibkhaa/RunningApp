package com.example.runnigapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.runnigapp.R
import com.example.runnigapp.databinding.FragmentRunBinding
import com.example.runnigapp.other.TrackingUtility
import com.example.runnigapp.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val TAG = "RunFragment"

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {
    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val requestPermissionLauncher =
        registerForActivityResult(RequestMultiplePermissions()) { permissions ->
//            permissions.entries.forEach {
//                Timber.d("key: ${it.key}")
//                Timber.d("value: ${it.value.toString()}")
//            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRunBinding.bind(view)
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        when {
            TrackingUtility.hasLocationPermissions(requireContext()) -> {
                return
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Timber.d("show rationale")
                val snackBar = Snackbar.make(
                    binding.root,
                    "Location permission is required for the app to work",
                    Snackbar.LENGTH_LONG
                )
                snackBar.setAction("Ok", View.OnClickListener {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        requestPermissionLauncher.launch(
                            TrackingUtility.locationPermissions
                        )
                    } else {
                        requestPermissionLauncher.launch(
                            TrackingUtility.locationPermissionsQ
                        )
                    }
                })
                snackBar.show()
            }
            else -> {
                Timber.d("Else")
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    requestPermissionLauncher.launch(
                        TrackingUtility.locationPermissions
                    )
                } else {
                    requestPermissionLauncher.launch(
                        TrackingUtility.locationPermissionsQ
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}