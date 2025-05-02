package vcmsa.projects.budgettracker.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgettracker.repository.ExpenseRepository

class ExpenseViewModelFactory(
    private val application: Application,
    private val expenseRepository: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(application, expenseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

