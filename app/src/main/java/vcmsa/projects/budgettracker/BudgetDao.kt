package vcmsa.projects.budgettracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import vcmsa.projects.budgettracker.model.Budget

@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userId = :userId")
    suspend fun getBudgetByUser(userId: Int): Budget?

    @Update
    suspend fun update(budget: Budget)
}

