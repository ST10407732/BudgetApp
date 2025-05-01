package vcmsa.projects.budgettracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.Category
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModel
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModelFactory

class BudgetDescriptionActivity : AppCompatActivity() {

    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()

    private lateinit var etCategoryName: EditText
    private lateinit var etBudgetAmount: EditText
    private lateinit var cbRollover: CheckBox
    private lateinit var cbSpecialEvent: CheckBox
    private lateinit var btnAddCategory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_description)

        // Setup input fields
        etCategoryName = findViewById(R.id.etCategoryName)
        etBudgetAmount = findViewById(R.id.etBudgetAmount)
        cbRollover = findViewById(R.id.cbRollover)
        cbSpecialEvent = findViewById(R.id.cbSpecialEvent)
        btnAddCategory = findViewById(R.id.btnAddCategory)

        val userId = intent.getIntExtra("USER_ID", 0)

        // Room database + DAO
        val db = BudgetDatabase.getDatabase(this)
        val factory = BudgetViewModelFactory(db.budgetDao(), db.expenseDao(), db.categoryDao(), userId)
        budgetViewModel = ViewModelProvider(this, factory)[BudgetViewModel::class.java]

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvCategories)
        recyclerView.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(this, categoryList)
        recyclerView.adapter = categoryAdapter

        // Load all categories
        loadCategories()

        // Observe budget info
        lifecycleScope.launch {
            budgetViewModel.budget.collect { budget ->
                budget?.let {
                    findViewById<TextView>(R.id.tvTotalBudget).text = "Total Budget: R${it.monthlyGoal}"
                    findViewById<TextView>(R.id.tvRolloverAmount).text = "Rollover Amount: R${it.rolloverAmount}"
                }
            }
        }

        // Add category button
        btnAddCategory.setOnClickListener {
            val name = etCategoryName.text.toString().trim()
            val amount = etBudgetAmount.text.toString().toDoubleOrNull()

            if (name.isBlank() || amount == null) {
                Toast.makeText(this, "Please enter valid name and amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newCategory = Category(
                name = name,
                monthlyBudget = amount,
                rollover = cbRollover.isChecked,
                isSpecialEvent = cbSpecialEvent.isChecked,
                userId = userId
            )

            lifecycleScope.launch {
                db.categoryDao().insertCategory(newCategory)
                loadCategories()
                etCategoryName.text.clear()
                etBudgetAmount.text.clear()
                cbRollover.isChecked = false
                cbSpecialEvent.isChecked = false
            }
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = budgetViewModel.getCategories()
            categoryList.clear()
            categoryList.addAll(categories)
            categoryAdapter.notifyDataSetChanged()
        }
    }
}
