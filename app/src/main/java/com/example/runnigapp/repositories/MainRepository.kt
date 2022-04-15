package com.example.runnigapp.repositories

import com.example.runnigapp.db.Run
import com.example.runnigapp.db.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {
    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getAllSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllSortedByDistance() = runDao.getAllRunsSortedByDistance()

    fun getAllSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    fun getAllSortedByAverageSpeed() = runDao.getAllRunsSortedByAverageSpeed()

    fun getAllSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDao.getTotalAverageSpeed()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()
}