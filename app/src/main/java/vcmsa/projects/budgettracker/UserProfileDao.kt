// UserProfileDao.kt
package vcmsa.projects.budgettracker.dao

import androidx.room.*
import vcmsa.projects.budgettracker.model.UserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profiles WHERE userId = :userId")
    suspend fun getUserProfile(userId: Int): UserProfile?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(userProfile: UserProfile)

    @Update
    suspend fun updateProfile(userProfile: UserProfile)
}
