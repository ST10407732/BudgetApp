package vcmsa.projects.budgettracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import vcmsa.projects.budgettracker.model.Budget
import vcmsa.projects.budgettracker.model.Category
import vcmsa.projects.budgettracker.model.Expense
import vcmsa.projects.budgettracker.model.User
import vcmsa.projects.budgettracker.util.Converters

@Database(
    entities = [User::class, Category::class, Expense::class, Budget::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class BudgetDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: BudgetDatabase? = null

        fun getDatabase(context: Context): BudgetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BudgetDatabase::class.java,
                    "budget_database"
                )
                    .fallbackToDestructiveMigration()  // drop & recreate on schema change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
