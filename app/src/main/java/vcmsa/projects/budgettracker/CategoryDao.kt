package vcmsa.projects.budgettracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vcmsa.projects.budgettracker.model.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

}
