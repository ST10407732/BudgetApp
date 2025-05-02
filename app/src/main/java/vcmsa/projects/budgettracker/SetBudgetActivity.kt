package vcmsa.projects.budgettracker

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.Budget
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModel
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModelFactory

class SetBudgetActivity : AppCompatActivity() {

    private lateinit var etBudget: EditText
    private lateinit var btnSaveBudget: Button
    private lateinit var tvMessage: TextView

    private lateinit var budgetViewModel: BudgetViewModel
    private var userId: Int = 0  // Declare, but don't assign here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_budget)

        // Get USER_ID from intent safely here
        userId = intent.getIntExtra("USER_ID", 0)

        // Initialize ViewModel inside onCreate with correct userId
        budgetViewModel = BudgetViewModelFactory(
            budgetDao = BudgetDatabase.getDatabase(application).budgetDao(),
            expenseDao = BudgetDatabase.getDatabase(application).expenseDao(),
            categoryDao = BudgetDatabase.getDatabase(application).categoryDao(),
            userId = userId
        ).create(BudgetViewModel::class.java)

        // Initialize views
        etBudget = findViewById(R.id.etBudget)
        btnSaveBudget = findViewById(R.id.btnSaveBudget)
        tvMessage = findViewById(R.id.tvMessage)

        btnSaveBudget.setOnClickListener {
            val budgetInput = etBudget.text.toString()

            if (TextUtils.isEmpty(budgetInput)) {
                tvMessage.text = "Please enter a valid budget amount."
                return@setOnClickListener
            }

            val budgetAmount = budgetInput.toDoubleOrNull()
            if (budgetAmount != null && budgetAmount > 0) {
                val budget = Budget(
                    userId = userId,
                    monthlyGoal = budgetAmount,
                    categoryLimit = emptyMap()
                )
                budgetViewModel.saveBudget(budget)

                Toast.makeText(this, "Budget saved!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                tvMessage.text = "Please enter a valid positive budget amount."
            }
        }
    }
}
