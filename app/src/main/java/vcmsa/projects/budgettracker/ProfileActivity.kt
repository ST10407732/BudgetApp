package vcmsa.projects.budgettracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgettracker.viewmodel.ProfileViewModel
import vcmsa.projects.budgettracker.util.SessionManager

class ProfileActivity : ComponentActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var achievementLevelTextView: TextView
    private lateinit var membershipDetailsTextView: TextView
    private lateinit var totalBalanceTextView: TextView
    private lateinit var totalDebtTextView: TextView
    private lateinit var monthlyIncomeTextView: TextView
    private lateinit var monthlyExpensesTextView: TextView
    private lateinit var netWorthTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var addProfileButton: Button

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        userNameTextView = findViewById(R.id.tvUserName)
        achievementLevelTextView = findViewById(R.id.tvAchievementLevel)
        membershipDetailsTextView = findViewById(R.id.tvMembershipDetails)
        totalBalanceTextView = findViewById(R.id.tvTotalBalance)
        totalDebtTextView = findViewById(R.id.tvTotalDebt)
        monthlyIncomeTextView = findViewById(R.id.tvMonthlyIncome)
        monthlyExpensesTextView = findViewById(R.id.tvMonthlyExpenses)
        netWorthTextView = findViewById(R.id.tvNetWorth)
        editProfileButton = findViewById(R.id.btnEditProfile)
        addProfileButton = findViewById(R.id.btnAddProfile)

        // Initialize ViewModel
        profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ProfileViewModel::class.java)

        // Get current userId from session
        val sessionManager = SessionManager(this)
        val currentUserId = sessionManager.getUserId()

        if (currentUserId != -1) {
            profileViewModel.loadUserProfile(currentUserId)
        }

        // Observe the profile
        profileViewModel.getUserProfile().observe(this) { user ->
            userNameTextView.text = user.fullName
            achievementLevelTextView.text = "Level: ${user.achievementLevel}"
            membershipDetailsTextView.text = "Member since: ${user.membershipSince}"
            totalBalanceTextView.text = "Total Balance: $${user.totalBalance}"
            totalDebtTextView.text = "Total Debt: $${user.totalDebt}"
            monthlyIncomeTextView.text = "Monthly Income: $${user.monthlyIncome}"
            monthlyExpensesTextView.text = "Monthly Expenses: $${user.monthlyExpenses}"
            netWorthTextView.text = "Net Worth: $${user.netWorth}"
        }

        // Launch Add Profile screen
        addProfileButton.setOnClickListener {
            val intent = Intent(this, AddProfileActivity::class.java)
            intent.putExtra("mode", "add")
            startActivity(intent)
        }

        // Launch Edit Profile screen
        editProfileButton.setOnClickListener {
            val intent = Intent(this, AddProfileActivity::class.java)
            intent.putExtra("mode", "edit")
            startActivity(intent)
        }
    }
}
