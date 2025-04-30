package vcmsa.projects.budgettracker.repository

import androidx.lifecycle.LiveData
import vcmsa.projects.budgettracker.database.ExpenseDao
import vcmsa.projects.budgettracker.model.ExpenseWithCategory

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    // A method to get expenses with categories in a specific time range
    fun getAllExpensesWithCategoryLive(): LiveData<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesWithCategoryLive()
    }

}
