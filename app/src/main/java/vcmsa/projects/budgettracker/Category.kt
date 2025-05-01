package vcmsa.projects.budgettracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val limit: Double = 0.0,
    val rollover: Boolean = false,
    val monthlyBudget: Double,
    val isSpecialEvent: Boolean = false,
    val userId: Int  // Add userId to the Category model
)
