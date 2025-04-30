package vcmsa.projects.budgettracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventBudgetAdapter(private val eventBudgets: Map<String, Double>) : RecyclerView.Adapter<EventBudgetAdapter.EventBudgetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventBudgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_budget, parent, false)
        return EventBudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventBudgetViewHolder, position: Int) {
        val eventName = eventBudgets.keys.elementAt(position)
        val eventAmount = eventBudgets[eventName]
        holder.bind(eventName, eventAmount)
    }

    override fun getItemCount() = eventBudgets.size

    inner class EventBudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.tvEventName)
        private val eventAmount: TextView = itemView.findViewById(R.id.tvEventAmount)

        fun bind(eventName: String, eventAmount: Double?) {
            this.eventName.text = eventName
            this.eventAmount.text = "Budget: $${eventAmount ?: 0.0}"
        }
    }
}
