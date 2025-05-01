package vcmsa.projects.budgettracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.budgettracker.model.Category

class CategoryAdapter(private val context: Context, private val categoryList: MutableList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // ViewHolder class to hold the views for each item in the RecyclerView
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvCategoryBudget: TextView = itemView.findViewById(R.id.tvCategoryBudget)
        val tvCategoryType: TextView = itemView.findViewById(R.id.tvCategoryType)
    }

    // Creates a new view holder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Binds data to the view holder
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.tvCategoryName.text = category.name
        holder.tvCategoryBudget.text = "Budget: R${category.monthlyBudget}"
        holder.tvCategoryType.text = if (category.isSpecialEvent) "Type: Special" else "Type: Regular"
    }

    // Returns the size of the list
    override fun getItemCount(): Int {
        return categoryList.size
    }

    // Optional: Update the data in the adapter when categories change
    fun updateCategories(newCategories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newCategories)
        notifyDataSetChanged()
    }
}
