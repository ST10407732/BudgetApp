package vcmsa.projects.budgettracker

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModel
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModelFactory
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.CategoryAdapter
import vcmsa.projects.budgettracker.EventBudgetAdapter

class BudgetDescriptionActivity : AppCompatActivity() {

    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var eventBudgetAdapter: EventBudgetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_description)

        // Replace with your logic to get the user ID
        val userId = 1 // TODO: Replace with actual user logic or pass via Intent

        // Get DAO instances from your Room database
        val db = BudgetDatabase.getDatabase(this)
        val budgetDao = db.budgetDao()
        val expenseDao = db.expenseDao()
        val categoryDao = db.categoryDao()

        // Create ViewModel with factory
        val factory = BudgetViewModelFactory(budgetDao, expenseDao, categoryDao, userId)
        budgetViewModel = ViewModelProvider(this, factory)[BudgetViewModel::class.java]

        // Set up RecyclerViews
        val categoriesRecyclerView: RecyclerView = findViewById(R.id.rvCategories)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)

        val eventBudgetsRecyclerView: RecyclerView = findViewById(R.id.rvEventBudgets)
        eventBudgetsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Observe flows using lifecycleScope
        lifecycleScope.launch {
            budgetViewModel.categories.collect { categories ->
                categoryAdapter = CategoryAdapter(categories)
                categoriesRecyclerView.adapter = categoryAdapter
            }
        }

        lifecycleScope.launch {
            budgetViewModel.budget.collect { budget ->
                budget?.let {
                    findViewById<TextView>(R.id.tvTotalBudget).text = "Total Budget: $${it.monthlyGoal}"
                    findViewById<TextView>(R.id.tvRolloverAmount).text = "Rollover Amount: $${it.rolloverAmount}"
                    eventBudgetAdapter = EventBudgetAdapter(it.eventBudgets)
                    eventBudgetsRecyclerView.adapter = eventBudgetAdapter
                }
            }
        }
    }

    fun onAddCategoryClicked(view: View) {
        // Handle Add Category action (you can open a new dialog or activity)
    }

    fun onAdjustBudgetClicked(view: View) {
        // Handle Adjust Budget action (you can open a dialog or activity)
    }
}
