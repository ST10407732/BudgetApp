package vcmsa.projects.budgettracker.repository

import android.content.Context
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.UserProfile

class ProfileRepository(context: Context) {

    private val userProfileDao = BudgetDatabase.getDatabase(context).userProfileDao()

    suspend fun getUserProfile(userId: Int): UserProfile? {
        return userProfileDao.getUserProfile(userId)
    }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(userProfile)
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateProfile(userProfile)
    }
}
