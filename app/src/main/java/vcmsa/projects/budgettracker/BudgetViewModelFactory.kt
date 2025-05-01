package vcmsa.projects.budgettracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgettracker.database.BudgetDao
import vcmsa.projects.budgettracker.database.CategoryDao
import vcmsa.projects.budgettracker.database.ExpenseDao

class BudgetViewModelFactory(
    private val budgetDao: BudgetDao,
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val userId: Int // Add the userId parameter here
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            BudgetViewModel(budgetDao, expenseDao, categoryDao, userId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
