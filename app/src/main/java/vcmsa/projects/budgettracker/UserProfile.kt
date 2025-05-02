// UserProfile.kt
package vcmsa.projects.budgettracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val userId: Int, // Matches User.id
    val fullName: String,
    val achievementLevel: Int,
    val membershipSince: String,
    val totalBalance: Double,
    val totalDebt: Double,
    val monthlyIncome: Double,
    val monthlyExpenses: Double,
    val netWorth: Double
)
