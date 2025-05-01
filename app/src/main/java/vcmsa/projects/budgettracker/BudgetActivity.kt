package vcmsa.projects.budgettracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.Category

class BudgetActivity : AppCompatActivity() {

    private lateinit var etCategoryName: EditText
    private lateinit var etMonthlyBudget: EditText
    private lateinit var cbRollover: CheckBox
    private lateinit var btnAddCategory: Button

    private lateinit var etSpecialEventName: EditText
    private lateinit var etSpecialEventBudget: EditText
    private lateinit var btnAddEvent: Button

    private lateinit var rvCategories: RecyclerView

    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        // Regular category inputs
        etCategoryName = findViewById(R.id.etCategoryName)
        etMonthlyBudget = findViewById(R.id.etMonthlyBudget)
        cbRollover = findViewById(R.id.cbRollover)
        btnAddCategory = findViewById(R.id.btnAddCategory)

        // Special event inputs
        etSpecialEventName = findViewById(R.id.etSpecialEventName)
        etSpecialEventBudget = findViewById(R.id.etSpecialEventBudget)
        btnAddEvent = findViewById(R.id.btnAddEvent)

        // RecyclerView setup
        rvCategories = findViewById(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(this, categoryList)
        rvCategories.adapter = categoryAdapter

        // Button actions
        btnAddCategory.setOnClickListener { addCategory(false) }
        btnAddEvent.setOnClickListener { addCategory(true) }

        loadCategories()
    }

    private fun addCategory(isSpecialEvent: Boolean) {
        val userId = intent.getIntExtra("USER_ID", 0)

        val name = if (isSpecialEvent) etSpecialEventName.text.toString().trim()
        else etCategoryName.text.toString().trim()

        val budgetText = if (isSpecialEvent) etSpecialEventBudget.text.toString()
        else etMonthlyBudget.text.toString()

        val budget = budgetText.toDoubleOrNull()
        val rollover = if (!isSpecialEvent) cbRollover.isChecked else false

        if (name.isBlank() || budget == null) {
            Toast.makeText(this, "Please enter valid name and budget", Toast.LENGTH_SHORT).show()
            return
        }

        val category = Category(
            name = name,
            monthlyBudget = budget,
            rollover = rollover,
            isSpecialEvent = isSpecialEvent,
            userId = userId
        )

        lifecycleScope.launch {
            val db = BudgetDatabase.getDatabase(applicationContext)
            db.categoryDao().insertCategory(category)
            loadCategories()
        }

        // Clear form
        if (isSpecialEvent) {
            etSpecialEventName.text.clear()
            etSpecialEventBudget.text.clear()
        } else {
            etCategoryName.text.clear()
            etMonthlyBudget.text.clear()
            cbRollover.isChecked = false
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val db = BudgetDatabase.getDatabase(applicationContext)
            val categories = db.categoryDao().getAllCategories()

            categoryList.clear()
            categoryList.addAll(categories)
            categoryAdapter.notifyDataSetChanged()
        }
    }
}
