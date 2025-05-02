package vcmsa.projects.budgettracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.budgettracker.adapter.ExpenseAdapter
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.repository.ExpenseRepository
import vcmsa.projects.budgettracker.viewmodel.ExpenseViewModel
import vcmsa.projects.budgettracker.viewmodel.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException
import vcmsa.projects.budgettracker.model.ExpenseWithCategory

class ExpensesListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var btnFilter: Button

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(
            application,
            ExpenseRepository(BudgetDatabase.getDatabase(this).expenseDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_list)

        recyclerView = findViewById(R.id.rvExpenses)
        recyclerView.layoutManager = LinearLayoutManager(this)

        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter

        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        btnFilter = findViewById(R.id.btnFilter)

        // Set click listeners for date selection
        etStartDate.setOnClickListener {
            showDatePickerDialog(etStartDate)
        }

        etEndDate.setOnClickListener {
            showDatePickerDialog(etEndDate)
        }

        btnFilter.setOnClickListener {
            val startDate = etStartDate.text.toString()
            val endDate = etEndDate.text.toString()

            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                filterExpensesByDate(startDate, endDate)
            } else {
                Toast.makeText(this, "Please select both start and end date", Toast.LENGTH_SHORT).show()
            }
        }

        loadAllExpensesWithCategory()
    }

    private fun showDatePickerDialog(targetEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            targetEditText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun filterExpensesByDate(startDate: String, endDate: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)

            if (start != null && end != null) {
                expenseViewModel.getExpensesByDateRange(start, end).observe(this) { expenses ->
                    if (expenses.isNullOrEmpty()) {
                        Toast.makeText(this, "No expenses found for the selected dates.", Toast.LENGTH_SHORT).show()
                    } else {
                        lifecycleScope.launch {
                            val expenseWithCategories = expenses.map { expense ->
                                val category = expenseViewModel.getCategoryById(expense.categoryId)
                                ExpenseWithCategory(expense, category)
                            }
                            expenseAdapter.submitList(expenseWithCategories)
                        }
                    }
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun loadAllExpensesWithCategory() {
        expenseViewModel.getAllExpensesWithCategoryLive().observe(this) { expenses ->
            if (expenses.isNullOrEmpty()) {
                Toast.makeText(this, "No expenses found.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ExpensesListActivity", "Loaded ${expenses.size} expenses")
                expenseAdapter.submitList(expenses)
            }
        }
    }
}
