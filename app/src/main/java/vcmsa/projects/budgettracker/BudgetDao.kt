package vcmsa.projects.budgettracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import vcmsa.projects.budgettracker.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert
    suspend fun insert(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userId = :userId")
    suspend fun getBudgetByUser(userId: Int): Budget?

    @Query("SELECT * FROM budgets WHERE userId = :userId LIMIT 1")
    fun getBudget(userId: Int): LiveData<Budget>  // Use the correct table name "budgets"

    @Query("SELECT * FROM budgets WHERE userId = :userId LIMIT 1")
    fun getBudgetForUser(userId: Int): Flow<Budget?>

    @Update
    suspend fun update(budget: Budget)
}
