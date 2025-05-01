package vcmsa.projects.budgettracker

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.Category

class CategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryList: MutableList<Category>
    private lateinit var btnAddCategory: Button
    private var userId: Int = 0  // Make sure this is properly initialized.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Retrieve the userId from Intent or SharedPreferences or however it's passed
        userId = intent.getIntExtra("USER_ID", 0)  // Ensure you pass the userId when starting this activity.

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rvCategories)
        recyclerView.layoutManager = LinearLayoutManager(this)
        categoryList = mutableListOf()
        categoryAdapter = CategoryAdapter(this, categoryList)
        recyclerView.adapter = categoryAdapter

        btnAddCategory = findViewById(R.id.btnAddCategory)
        btnAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }

        loadCategories()
    }

    private fun showAddCategoryDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCategoryName)
        val etBudget = dialogView.findViewById<EditText>(R.id.etMonthlyBudget)
        val cbRollover = dialogView.findViewById<CheckBox>(R.id.cbRollover)
      //  val cbSpecialEvent = dialogView.findViewById<CheckBox>(R.id.cbSpecialEvent)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnAdd = dialogView.findViewById<Button>(R.id.btnAddCategory)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelCategory)

        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val budget = etBudget.text.toString().toDoubleOrNull()
            val rollover = cbRollover.isChecked
          //  val isSpecialEvent = cbSpecialEvent.isChecked

            if (name.isBlank() || budget == null) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pass userId here when creating the new category
            val newCategory = Category(
                name = name,
                monthlyBudget = budget,
                rollover = rollover,
               // isSpecialEvent = isSpecialEvent,
                userId = userId // Pass the userId
            )

            lifecycleScope.launch {
                val db = BudgetDatabase.getDatabase(applicationContext)
                db.categoryDao().insertCategory(newCategory)
                loadCategories() // Reload categories after inserting
            }

            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val db = BudgetDatabase.getDatabase(applicationContext)
            val categories = db.categoryDao().getCategoriesForUser(userId)

            categoryList.clear()
            categoryList.addAll(categories)
            categoryAdapter.notifyDataSetChanged()  // Notify the adapter that the data has changed
        }
    }
}
