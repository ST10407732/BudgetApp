package vcmsa.projects.budgettracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.Category

class CategoryActivity : AppCompatActivity() {

    private lateinit var etCategoryName: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var lvCategories: ListView
    private lateinit var categoriesAdapter: ArrayAdapter<String>
    private lateinit var categoriesList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        etCategoryName = findViewById(R.id.etCategoryName)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        lvCategories = findViewById(R.id.lvCategories)

        categoriesList = mutableListOf()
        categoriesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoriesList)
        lvCategories.adapter = categoriesAdapter

        // Add category to the database
        btnAddCategory.setOnClickListener {
            val categoryName = etCategoryName.text.toString().trim()
            if (categoryName.isNotBlank()) {
                val category = Category(name = categoryName)
                lifecycleScope.launch {
                    // Insert category into DB
                    val db = BudgetDatabase.getDatabase(applicationContext)
                    db.categoryDao().insert(category)

                    // Update ListView
                    loadCategories()

                    // Clear input field
                    etCategoryName.text.clear()
                }
            } else {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }

        // Load categories from the database
        loadCategories()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val db = BudgetDatabase.getDatabase(applicationContext)
            val categories = db.categoryDao().getAllCategories()

            // Update ListView with category names
            categoriesList.clear()
            categories.forEach { category ->
                categoriesList.add(category.name)
            }
            categoriesAdapter.notifyDataSetChanged()
        }
    }
}
