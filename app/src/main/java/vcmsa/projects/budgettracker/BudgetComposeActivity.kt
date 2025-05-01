package vcmsa.projects.budgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import vcmsa.projects.budgettracker.database.BudgetDao
import vcmsa.projects.budgettracker.database.CategoryDao
import vcmsa.projects.budgettracker.database.ExpenseDao
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModel
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModelFactory
import vcmsa.projects.budgettracker.ui.screens.BudgetDescriptionScreen

class BudgetComposeActivity : ComponentActivity() {

    private lateinit var viewModel: BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize your Room database (BudgetDatabase should be your Room Database class)
        val db = Room.databaseBuilder(
            applicationContext,
            BudgetDatabase::class.java,
            "budget-database"
        ).build()

        // Get DAOs from the Room database
        val budgetDao: BudgetDao = db.budgetDao()
        val expenseDao: ExpenseDao = db.expenseDao()
        val categoryDao: CategoryDao = db.categoryDao()

        // Replace this with the actual user ID
        val userId = 1

        // Initialize ViewModel using the factory
        val factory = BudgetViewModelFactory(budgetDao, expenseDao, categoryDao, userId)
        viewModel = ViewModelProvider(this, factory).get(BudgetViewModel::class.java)

        setContent {
            BudgetDescriptionScreen(viewModel = viewModel)
        }
    }
}
