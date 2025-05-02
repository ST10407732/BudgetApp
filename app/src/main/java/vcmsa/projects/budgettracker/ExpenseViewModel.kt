package vcmsa.projects.budgettracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import vcmsa.projects.budgettracker.model.ExpenseWithCategory
import vcmsa.projects.budgettracker.model.Expense
import vcmsa.projects.budgettracker.model.Category
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ExpenseViewModel(application: Application, private val repository: ExpenseRepository) : AndroidViewModel(application) {

    // Initialize CategoryDao from the BudgetDatabase
    private val categoryDao = BudgetDatabase.getDatabase(application).categoryDao()

    fun getAllExpenses(): LiveData<List<Expense>> {
        return repository.getAllExpenses()
    }

    // ✅ Make this suspend so it's safe to call from a coroutine
    suspend fun getCategoryById(categoryId: Int): Category {
        return withContext(Dispatchers.IO) {
            categoryDao.getCategoryById(categoryId)
        }
    }

    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return repository.getExpensesByDateRange(startDate, endDate)
    }

    fun getAllExpensesWithCategoryLive(): LiveData<List<ExpenseWithCategory>> {
        return repository.getAllExpensesWithCategoryLive()
    }
}
