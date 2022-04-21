package com.example.runnigapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnigapp.db.Run
import com.example.runnigapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
): ViewModel(){
    val runsSortedByDate = mainRepository.getAllSortedByDate()
    fun insertRun(run: Run)=viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}