package vcmsa.projects.budgettracker.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import vcmsa.projects.budgettracker.database.BudgetDatabase
import vcmsa.projects.budgettracker.model.UserProfile
import vcmsa.projects.budgettracker.repository.ProfileRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userProfileLiveData = MutableLiveData<UserProfile>()
    private val userProfileDao = BudgetDatabase.getDatabase(application).userProfileDao()
    private val profileRepository = ProfileRepository(application)


    // ProfileViewModel
    fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            val profile = profileRepository.getUserProfile(userId)
            profile?.let {
                userProfileLiveData.postValue(it)
            }
        }
    }


    fun getUserProfile(): LiveData<UserProfile> {
        return userProfileLiveData
    }
    fun saveUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            profileRepository.saveUserProfile(userProfile)
        }
    }

    fun generateMonthlyReport() {
        // TODO: Implement real report generation logic (e.g., PDF export or CSV)
    }
}
