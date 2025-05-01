package vcmsa.projects.budgettracker.model



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val monthlyGoal: Double,
    val categoryLimit: Map<String, Double>,
    val rolloverAmount: Double = 0.0,
    val eventBudgets: Map<String, Double> = emptyMap(),

)
