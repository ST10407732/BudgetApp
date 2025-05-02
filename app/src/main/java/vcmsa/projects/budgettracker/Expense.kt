package vcmsa.projects.budgettracker.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,                        // ✅ newly added
    val amount: Double,
    val date: Long,
    val startTime: Long?,
    val endTime: Long?,
    val description: String,
    val categoryId: Int,
    val paymentMethod: String,
    val latitude: Double?,
    val longitude: Double?,
    val tags: String?,
    val notes: String?,
    val receiptPhoto: String? ,


    val category: String,  // Make sure this field exists and matches the query

)

