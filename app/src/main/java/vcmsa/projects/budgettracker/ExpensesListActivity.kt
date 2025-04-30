package vcmsa.projects.budgettracker

import android.os.Bundle
import android.widget.Toast
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.budgettracker.adapter.ExpenseAdapter
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.repository.ExpenseRepository
import vcmsa.projects.budgettracker.viewmodel.ExpenseViewModel
import vcmsa.projects.budgettracker.viewmodel.ExpenseViewModelFactory

class ExpensesListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(
            ExpenseRepository(
                BudgetDatabase.getDatabase(this).expenseDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_list)

        recyclerView = findViewById(R.id.rvExpenses)
        recyclerView.layoutManager = LinearLayoutManager(this)

        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter

        loadAllExpensesWithCategory()
    }

    private fun loadAllExpensesWithCategory() {
        expenseViewModel.getAllExpenses().observe(this) { expenses ->
            if (expenses.isNullOrEmpty()) {
                Toast.makeText(this, "No expenses found.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ExpensesListActivity", "Loaded ${expenses.size} expenses")
                expenseAdapter.submitList(expenses)
            }
        }
    }
}
