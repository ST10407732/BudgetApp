package vcmsa.projects.budgettracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.budgettracker.SetBudgetActivity
import android.view.animation.AnimationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    private lateinit var tvWelcome: TextView
    private lateinit var btnCategories: Button
    private lateinit var btnAddExpense: Button
    private lateinit var btnViewExpenses: Button
    private lateinit var btnBudget: Button
    private lateinit var btnInputIncome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
        // Initialize buttons
        btnCategories = findViewById(R.id.btnCategories)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnViewExpenses = findViewById(R.id.btnViewExpenses)
        btnBudget = findViewById(R.id.btnBudget)
        btnInputIncome = findViewById(R.id.btnInputIncome)
        tvWelcome = findViewById(R.id.tvWelcome)
        // Load the button press animation
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)

        // Set the welcome text
        val username = intent.getStringExtra("USERNAME") ?: "User"
        tvWelcome.text = "Welcome, $username 👋"

        // Apply animation when buttons are pressed
        btnCategories.setOnClickListener {
            btnCategories.startAnimation(pressAnim)
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        btnAddExpense.setOnClickListener {
            btnAddExpense.startAnimation(pressAnim)
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        btnViewExpenses.setOnClickListener {
            btnViewExpenses.startAnimation(pressAnim)
            startActivity(Intent(this, ExpensesListActivity::class.java))
        }

        btnBudget.setOnClickListener {
            btnBudget.startAnimation(pressAnim)
            startActivity(Intent(this, BudgetDescriptionActivity::class.java))
        }

        btnInputIncome.setOnClickListener {
            btnInputIncome.startAnimation(pressAnim)
            val intent = Intent(this, SetBudgetActivity::class.java)
            startActivity(intent)
        }
    }
        private fun setupBottomNavigation() {
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNavigation.selectedItemId = R.id.nav_dashboard

            bottomNavigation.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
                    R.id.nav_dashboard -> return@setOnItemSelectedListener true
                    R.id.nav_add_expense -> startActivity(Intent(this, AddExpenseActivity::class.java))
                    // R.id.nav_budget_health -> startActivity(Intent(this, BudgetHealthActivity::class.java))
                    R.id.nav_view_expenses -> startActivity(Intent(this, ExpensesListActivity::class.java))
                }
                true
            }
        }

    }
