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
                // Correctly call the function from ViewModel
                val spentForCategory by produceState(initialValue = 0.0, key1 = category.name) {
                    // Trigger loading category spending in ViewModel
                    viewModel.loadSpentForCategory(category.name)
                    value = viewModel.categorySpent.value ?: 0.0
                }

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
fun AddEditCategoryScreen(
    onSave: (String, Double, Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var categoryAmount by remember { mutableStateOf("") }
    var rollover by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Removed color resources
            .padding(16.dp)
    ) {
        Text("Add Category", color = Color.White, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Category Name", color = Color.White) }, // Removed color resource
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = categoryAmount,
            onValueChange = { categoryAmount = it },
            label = { Text("Monthly Budget", color = Color.White) }, // Removed color resource
            modifier = Modifier.fillMaxWidth(),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rollover,
                onCheckedChange = { rollover = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF9800)) // Directly use color
            )
            Text("Rollover unused budget", color = Color.White) // Removed color resource
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancel", color = Color.White) // Removed color resource
            }
            Button(onClick = {
                val amount = categoryAmount.toDoubleOrNull() ?: 0.0
                onSave(categoryName, amount, rollover)
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) { // Direct color
                Text("Add Category", color = Color.Black) // Removed color resource
            }
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
