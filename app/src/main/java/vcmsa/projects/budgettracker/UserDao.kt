package vcmsa.projects.budgettracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vcmsa.projects.budgettracker.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUser(username: String, password: String): User?

    // ✅ NEW: Check if username already exists (for registration)
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}
