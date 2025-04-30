package vcmsa.projects.budgettracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import vcmsa.projects.budgettracker.model.Expense
import vcmsa.projects.budgettracker.model.ExpenseWithCategory


@Dao
interface ExpenseDao {

    @Transaction
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    fun getExpensesWithCategoryInPeriod(
        startDate: Long,
        endDate: Long
    ): LiveData<List<ExpenseWithCategory>>

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    suspend fun getExpensesInPeriod(startDate: Long, endDate: Long): List<Expense>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpensesAmount(): Float
    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getAllExpensesForUser(userId: Int): List<Expense>

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    suspend fun getTotalSpent(userId: Int): Double?



    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId AND category = :categoryName")
    suspend fun getSpentForCategory(userId: Int, categoryName: String): Double
    // New method to get all expenses
    @Transaction
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpensesWithCategoryLive(): LiveData<List<ExpenseWithCategory>>
}

