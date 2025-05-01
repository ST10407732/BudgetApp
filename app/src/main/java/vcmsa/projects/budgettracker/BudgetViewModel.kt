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
import androidx.lifecycle.LiveData


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

    init {
        loadBudget()
        loadCategories()
        calculateTotalSpent()
    }

    private fun loadBudget() {
        viewModelScope.launch {
            _budget.value = budgetDao.getBudgetByUser(userId)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryDao.getAllCategories()
        }
    }
    // Make getCategories a suspend function
    suspend fun getCategories(): List<Category> {
        // Fetch categories for the specific userId from your database
        return categoryDao.getCategoriesForUser(userId)
    }


    private fun calculateTotalSpent() {
        viewModelScope.launch {
            _totalSpent.value = expenseDao.getTotalSpent(userId) ?: 0.0
        }
    }
    // Save the budget
    fun saveBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.insert(budget)
        }
    }

    // Get the current budget (optional if you want to show it on the screen)
    fun getBudget(): LiveData<Budget> {
        return budgetDao.getBudget(userId)
    }

    //  Safe coroutine-based way to get category spending
    fun loadSpentForCategory(categoryName: String) {
        viewModelScope.launch {
            _categorySpent.value = expenseDao.getSpentForCategory(userId, categoryName)
        }
    }
}
