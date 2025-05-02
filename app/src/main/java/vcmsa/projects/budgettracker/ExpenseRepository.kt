package vcmsa.projects.budgettracker.repository

import androidx.lifecycle.LiveData
import vcmsa.projects.budgettracker.database.ExpenseDao
import vcmsa.projects.budgettracker.model.Expense
import vcmsa.projects.budgettracker.model.ExpenseWithCategory
import java.util.Date // ✅ FIXED IMPORT

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun getAllExpenses(): LiveData<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    fun getExpensesByDateRange(startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate.time, endDate.time)
    }

    fun getAllExpensesWithCategoryLive(): LiveData<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesWithCategoryLive()
    }
}
