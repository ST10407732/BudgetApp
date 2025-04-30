package vcmsa.projects.budgettracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import vcmsa.projects.budgettracker.model.ExpenseWithCategory
import vcmsa.projects.budgettracker.repository.ExpenseRepository

class ExpenseViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    // Function to get expenses with category in a specific period
    fun getAllExpenses(): LiveData<List<ExpenseWithCategory>> {
        return expenseRepository.getAllExpensesWithCategoryLive()
    }


}
