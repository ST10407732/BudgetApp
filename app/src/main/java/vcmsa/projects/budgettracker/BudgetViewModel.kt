package vcmsa.projects.budgettracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDao
import vcmsa.projects.budgettracker.database.ExpenseDao
import vcmsa.projects.budgettracker.database.CategoryDao
import vcmsa.projects.budgettracker.model.Budget
import vcmsa.projects.budgettracker.model.Category

class BudgetViewModel(
    private val budgetDao: BudgetDao,
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val userId: Int
) : ViewModel() {

    private val _budget = MutableStateFlow<Budget?>(null)
    val budget: StateFlow<Budget?> get() = _budget

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _totalSpent = MutableStateFlow(0.0)
    val totalSpent: StateFlow<Double> get() = _totalSpent

    private val _categorySpent = MutableStateFlow<Map<String, Double>>(emptyMap())
    val categorySpent: StateFlow<Map<String, Double>> get() = _categorySpent

    init {
        loadBudget()
        loadCategories()
        loadTotalSpent()
        loadSpentForCategories()
    }

    // Load the user's budget from the BudgetDao
    private fun loadBudget() {
        viewModelScope.launch {
            _budget.value = budgetDao.getBudgetByUser(userId)
        }
    }

    // Load all categories from the CategoryDao
    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryDao.getAllCategories()
        }
    }

    // Load total spent amount using ExpenseDao
    private fun loadTotalSpent() {
        viewModelScope.launch {
            expenseDao.getTotalSpent(userId)?.let {
                _totalSpent.value = it
            }
        }
    }

    // Load the spent amount for each category and update categorySpent
    private fun loadSpentForCategories() {
        viewModelScope.launch {
            val categorySpentMap = mutableMapOf<String, Double>()
            for (category in _categories.value) {
                val spentForCategory = expenseDao.getSpentForCategory(userId, category.name) ?: 0.0
                categorySpentMap[category.name] = spentForCategory
            }
            _categorySpent.value = categorySpentMap
        }
    }

    // Update the monthly budget goal
    fun updateMonthlyBudget(budgetId: Int, newMonthlyGoal: Double) {
        viewModelScope.launch {
            val budget = budgetDao.getBudgetByUser(userId)
            if (budget != null) {
                val updatedBudget = budget.copy(monthlyGoal = newMonthlyGoal)
                budgetDao.update(updatedBudget)
            }
        }
    }

    // Update category limit for specific categories
    fun updateCategoryLimit(budgetId: Int, category: String, newLimit: Double) {
        viewModelScope.launch {
            val budget = budgetDao.getBudgetByUser(userId)
            if (budget != null) {
                val updatedLimits = budget.categoryLimit.toMutableMap()
                updatedLimits[category] = newLimit
                val updatedBudget = budget.copy(categoryLimit = updatedLimits)
                budgetDao.update(updatedBudget)
            }
        }
    }

    // Update rollover amount
    fun updateRolloverAmount(budgetId: Int, newRolloverAmount: Double) {
        viewModelScope.launch {
            val budget = budgetDao.getBudgetByUser(userId)
            if (budget != null) {
                val updatedBudget = budget.copy(rolloverAmount = newRolloverAmount)
                budgetDao.update(updatedBudget)
            }
        }
    }

    // Update event budgets
    fun updateEventBudget(budgetId: Int, eventName: String, newAmount: Double) {
        viewModelScope.launch {
            val budget = budgetDao.getBudgetByUser(userId)
            if (budget != null) {
                val updatedEventBudgets = budget.eventBudgets.toMutableMap()
                updatedEventBudgets[eventName] = newAmount
                val updatedBudget = budget.copy(eventBudgets = updatedEventBudgets)
                budgetDao.update(updatedBudget)
            }
        }
    }

    // Fetch spent for a specific category
    fun getSpentForCategory(categoryName: String): Double {
        var spentAmount = 0.0
        viewModelScope.launch {
            spentAmount = expenseDao.getSpentForCategory(userId, categoryName) ?: 0.0
        }
        return spentAmount
    }
}
