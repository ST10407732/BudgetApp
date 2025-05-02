package vcmsa.projects.budgettracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDao
import vcmsa.projects.budgettracker.database.CategoryDao
import vcmsa.projects.budgettracker.database.ExpenseDao
import vcmsa.projects.budgettracker.model.Budget
import vcmsa.projects.budgettracker.model.Category
import kotlinx.coroutines.flow.Flow

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

    private val _categorySpent = MutableStateFlow<Double?>(null)
    val categorySpent: StateFlow<Double?> get() = _categorySpent

    private val _remainingBudget = MutableStateFlow<Double?>(null)
    val remainingBudget: StateFlow<Double?> get() = _remainingBudget

    init {
        loadBudget()
        loadCategories()
        calculateTotalSpent()
        calculateRemainingBudget()
    }

    private fun loadBudget() {
        viewModelScope.launch {
            _budget.value = budgetDao.getBudgetByUser(userId)
        }
    }

    private fun calculateRemainingBudget() {
        viewModelScope.launch {
            val budgetAmount = budgetDao.getBudgetByUser(userId)?.monthlyGoal ?: 0.0
            val spentAmount = expenseDao.getTotalSpent(userId) ?: 0.0
            _remainingBudget.value = budgetAmount - spentAmount
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryDao.getAllCategories()
        }
    }

    suspend fun getCategories(): List<Category> {
        return categoryDao.getCategoriesForUser(userId)
    }
    fun getBudgetForUser(userId: Int): Flow<Budget?> {
        return budgetDao.getBudgetForUser(userId)
    }

    fun getTotalSpent(): Flow<Double> {
        return expenseDao.getTotalSpentFlow(userId)
    }


    private fun calculateTotalSpent() {
        viewModelScope.launch {
            _totalSpent.value = expenseDao.getTotalSpent(userId) ?: 0.0
        }
    }

    fun saveBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.insert(budget)
            _budget.value = budget
            calculateRemainingBudget()
        }
    }

    fun loadSpentForCategory(categoryName: String) {
        viewModelScope.launch {
            _categorySpent.value = expenseDao.getSpentForCategory(userId, categoryName)
        }
    }
}
