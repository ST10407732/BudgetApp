package vcmsa.projects.budgettracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgettracker.viewmodel.ProfileViewModel
import vcmsa.projects.budgettracker.model.UserProfile

class AddProfileActivity : ComponentActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var achievementLevelEditText: EditText
    private lateinit var membershipDetailsEditText: EditText
    private lateinit var totalBalanceEditText: EditText
    private lateinit var totalDebtEditText: EditText
    private lateinit var monthlyIncomeEditText: EditText
    private lateinit var monthlyExpensesEditText: EditText
    private lateinit var netWorthEditText: EditText
    private lateinit var saveProfileButton: Button

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)

        // Initialize views
        fullNameEditText = findViewById(R.id.etFullName)
        achievementLevelEditText = findViewById(R.id.etAchievementLevel)
        membershipDetailsEditText = findViewById(R.id.etMembershipDetails)
        totalBalanceEditText = findViewById(R.id.etTotalBalance)
        totalDebtEditText = findViewById(R.id.etTotalDebt)
        monthlyIncomeEditText = findViewById(R.id.etMonthlyIncome)
        monthlyExpensesEditText = findViewById(R.id.etMonthlyExpenses)
        netWorthEditText = findViewById(R.id.etNetWorth)
        saveProfileButton = findViewById(R.id.btnSaveProfile)

        // Initialize ViewModel
        profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ProfileViewModel::class.java]

        saveProfileButton.setOnClickListener {
            try {
                val fullName = fullNameEditText.text.toString().trim()
                val achievementLevel = achievementLevelEditText.text.toString().toInt()
                val membershipDetails = membershipDetailsEditText.text.toString().trim()
                val totalBalance = totalBalanceEditText.text.toString().toDouble()
                val totalDebt = totalDebtEditText.text.toString().toDouble()
                val monthlyIncome = monthlyIncomeEditText.text.toString().toDouble()
                val monthlyExpenses = monthlyExpensesEditText.text.toString().toDouble()
                val netWorth = netWorthEditText.text.toString().toDouble()

                // TODO: Replace this with the actual logged-in user ID
                val userId = 1

                val userProfile = UserProfile(
                    userId = userId,
                    fullName = fullName,
                    achievementLevel = achievementLevel,
                    membershipSince = membershipDetails,
                    totalBalance = totalBalance,
                    totalDebt = totalDebt,
                    monthlyIncome = monthlyIncome,
                    monthlyExpenses = monthlyExpenses,
                    netWorth = netWorth
                )

                profileViewModel.saveUserProfile(userProfile)
                Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error saving profile: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
