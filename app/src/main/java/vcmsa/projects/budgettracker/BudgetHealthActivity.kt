package vcmsa.projects.budgettracker

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vcmsa.projects.budgettracker.database.BudgetDatabase

class BudgetHealthActivity : AppCompatActivity() {

    private lateinit var tvSpent: TextView
    private lateinit var tvRemaining: TextView
    private lateinit var tvProjected: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var horizontalBarChart: HorizontalBarChart
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_health)

        tvSpent = findViewById(R.id.tvSpent)
        tvRemaining = findViewById(R.id.tvRemaining)
        tvProjected = findViewById(R.id.tvProjected)
        progressBar = findViewById(R.id.budgetHealthProgressBar)
        horizontalBarChart = findViewById(R.id.horizontalBarChart)
        statusTextView = findViewById(R.id.statusTextView)

        setupBudgetHealth()
        setupHorizontalBarChart()
        setupBottomNavigation()
    }

    private fun setupBudgetHealth() {
        lifecycleScope.launch(Dispatchers.IO) {
            val expenseDao = BudgetDatabase.getDatabase(applicationContext).expenseDao()
            val totalSpent = expenseDao.getTotalExpensesAmount()
            val monthlyBudget = 5000f // Static budget or fetch dynamically

            val spentPercentage = (totalSpent / monthlyBudget) * 100

            withContext(Dispatchers.Main) {
                tvSpent.text = "Spent: R${String.format("%.2f", totalSpent)}"
                tvRemaining.text = "Remaining: R${String.format("%.2f", monthlyBudget - totalSpent)}"

                if (totalSpent > monthlyBudget) {
                    tvProjected.text = "Projected Overspending: R${String.format("%.2f", totalSpent - monthlyBudget)}"
                    statusTextView.text = "Budget Status: Overspending!"
                    progressBar.progressTintList = ColorStateList.valueOf(Color.RED)
                } else {
                    tvProjected.text = "Projected Overspending: R0"
                    statusTextView.text = "Budget Status: Healthy"
                    progressBar.progressTintList = ColorStateList.valueOf(Color.parseColor("#00FF00"))
                }

                progressBar.progress = spentPercentage.toInt().coerceAtMost(100)
            }
        }
    }

    private fun setupHorizontalBarChart() {
        lifecycleScope.launch(Dispatchers.IO) {
            val expenseDao = BudgetDatabase.getDatabase(applicationContext).expenseDao()
            val expensesWithCategories = expenseDao.getAllExpensesWithCategoryLive().value ?: emptyList()


            val categoryTotals = mutableMapOf<String, Float>()
            for (expenseWithCategory in expensesWithCategories) {
                val categoryName = expenseWithCategory.category.name
                val amount = expenseWithCategory.expense.amount.toFloat()
                categoryTotals[categoryName] = categoryTotals.getOrDefault(categoryName, 0f) + amount
            }

            val entries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            categoryTotals.entries.forEachIndexed { index, (category, totalAmount) ->
                entries.add(BarEntry(index.toFloat(), totalAmount))
                labels.add(category)
            }

            val barDataSet = BarDataSet(entries, "Expenses").apply {
                colors = listOf(
                    Color.parseColor("#FFA500"), // Orange
                    Color.parseColor("#FF6347"), // Tomato
                    Color.parseColor("#87CEEB")  // Sky Blue
                )
                valueTextColor = Color.WHITE
                valueTextSize = 12f
            }

            val barData = BarData(barDataSet)

            withContext(Dispatchers.Main) {
                horizontalBarChart.data = barData
                horizontalBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                horizontalBarChart.xAxis.textColor = Color.WHITE
                horizontalBarChart.axisLeft.textColor = Color.WHITE
                horizontalBarChart.axisRight.textColor = Color.WHITE
                horizontalBarChart.legend.textColor = Color.WHITE
                horizontalBarChart.description.isEnabled = false
                horizontalBarChart.invalidate()
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_budget_health

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.nav_dashboard -> startActivity(Intent(this, DashboardActivity::class.java))
                R.id.nav_add_expense -> startActivity(Intent(this, AddExpenseActivity::class.java))
                R.id.nav_budget_health -> return@setOnItemSelectedListener true
                R.id.nav_view_expenses -> startActivity(Intent(this, ExpensesListActivity::class.java))
            }
            true
        }
    }
}
