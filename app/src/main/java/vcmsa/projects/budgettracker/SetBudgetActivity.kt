package vcmsa.projects.budgettracker

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.Budget
import vcmsa.projects.budgettracker.model.Category
import vcmsa.projects.budgettracker.R

import vcmsa.projects.budgettracker.viewmodel.BudgetViewModel
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModelFactory

class SetBudgetActivity : AppCompatActivity() {

    private lateinit var etBudget: EditText
    private lateinit var btnSaveBudget: Button
    private lateinit var tvMessage: TextView

    // Assuming you retrieve the user ID from a session or login
    val userId = intent.getIntExtra("USER_ID", 0)

    // Use the ViewModel with factory
    private val budgetViewModel: BudgetViewModel by viewModels {
        BudgetViewModelFactory(
            budgetDao = BudgetDatabase.getDatabase(application).budgetDao(),
            expenseDao = BudgetDatabase.getDatabase(application).expenseDao(),
            categoryDao = BudgetDatabase.getDatabase(application).categoryDao(),
            userId = userId // Pass userId here
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_budget)

        // Initialize views
        etBudget = findViewById(R.id.etBudget)
        btnSaveBudget = findViewById(R.id.btnSaveBudget)
        tvMessage = findViewById(R.id.tvMessage)

        // Set up click listener to save budget
        btnSaveBudget.setOnClickListener {
            val budgetInput = etBudget.text.toString()

            if (TextUtils.isEmpty(budgetInput)) {
                tvMessage.text = "Please enter a valid budget amount."
                return@setOnClickListener
            }

            val budgetAmount = budgetInput.toDoubleOrNull()
            if (budgetAmount != null && budgetAmount > 0) {
                // Save the budget to the ViewModel/Database
                val budget = Budget(
                    userId = userId,
                    monthlyGoal = budgetAmount,
                    categoryLimit = emptyMap()  // Pass an empty map if no categories are defined
                )

                // Call ViewModel to save the budget
                budgetViewModel.saveBudget(budget)

                // Show a success message
                tvMessage.text = "Budget saved successfully!"
                Toast.makeText(this, "Budget saved!", Toast.LENGTH_SHORT).show()

                // Optionally, go back to the previous screen
                finish()
            } else {
                tvMessage.text = "Please enter a valid positive budget amount."
            }
        }
    }
}