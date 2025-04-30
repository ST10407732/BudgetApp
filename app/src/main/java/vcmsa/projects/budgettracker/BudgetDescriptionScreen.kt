package vcmsa.projects.budgettracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import vcmsa.projects.budgettracker.viewmodel.BudgetViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun BudgetDescriptionScreen(viewModel: BudgetViewModel) {
    // Collecting state from ViewModel
    val budget by viewModel.budget.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val totalSpent by viewModel.totalSpent.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Budget Overview", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Summary Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                val goal = budget?.monthlyGoal ?: 0.0
                val remaining = goal - totalSpent

                Text("Total Budget: ${formatCurrency(goal)}")
                Text("Amount Spent: ${formatCurrency(totalSpent)}")
                Text("Remaining Balance: ${formatCurrency(remaining)}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Breakdown
        Text("Category Budgets", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(categories) { category ->
                val spentForCategory = viewModel.getSpentForCategory(category.name)
                CategoryItem(
                    categoryName = category.name,
                    limit = budget?.categoryLimit?.get(category.name) ?: 0.0,
                    spent = spentForCategory
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Navigate to Add/Edit Category */ }) {
                Text("Manage Categories")
            }
            Button(onClick = { /* Navigate to Adjust Budget */ }) {
                Text("Adjust Budget")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { /* Navigate to Add Special Event Budget */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Event Budget")
        }
    }
}

@Composable
fun CategoryItem(categoryName: String, limit: Double, spent: Double) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(categoryName, style = MaterialTheme.typography.bodyLarge)
        Text("Limit: ${formatCurrency(limit)}")
        Text("Spent: ${formatCurrency(spent)}")
    }
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount)
}
