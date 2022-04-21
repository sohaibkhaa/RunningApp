package com.example.runnigapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.runnigapp.R
import com.example.runnigapp.adapters.RunAdapter
import com.example.runnigapp.databinding.FragmentRunBinding
import com.example.runnigapp.other.SortType
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
    private lateinit var runAdapter: RunAdapter

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
        setUpRecyclerView()

        when (viewModel.sortType) {
            SortType.DATE -> binding.spFilter.setSelection(0)
            SortType.RUNNING_TIME -> binding.spFilter.setSelection(1)
            SortType.DISTANCE -> binding.spFilter.setSelection(2)
            SortType.AVG_SPEED -> binding.spFilter.setSelection(3)
            SortType.CALORIES_BURNED -> binding.spFilter.setSelection(4)
        }

        binding.spFilter.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                when(pos){
                    0->viewModel.sortRuns(SortType.DATE)
                    1->viewModel.sortRuns(SortType.RUNNING_TIME)
                    2->viewModel.sortRuns(SortType.DISTANCE)
                    3->viewModel.sortRuns(SortType.AVG_SPEED)
                    4->viewModel.sortRuns(SortType.CALORIES_BURNED)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        viewModel.runs.observe(viewLifecycleOwner) {
            runAdapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
        requestPermissions()
    }

    private fun setUpRecyclerView() = binding.rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
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