package vcmsa.projects.budgettracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnCategories: Button
    private lateinit var btnAddExpense: Button
    private lateinit var btnViewExpenses: Button
    private lateinit var btnBudget: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get the username passed from DashboardActivity
        val username = intent.getStringExtra("USERNAME") ?: "User"

        tvWelcome = findViewById(R.id.tvWelcome)
        btnCategories = findViewById(R.id.btnCategories)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnViewExpenses = findViewById(R.id.btnViewExpenses)

        tvWelcome.text = "Welcome, $username 👋"

        btnCategories.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        btnViewExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesListActivity::class.java))
        }
        btnBudget = findViewById(R.id.btnBudget)

        btnBudget.setOnClickListener {
            startActivity(Intent(this, BudgetDescriptionActivity::class.java))
        }

    }
}
