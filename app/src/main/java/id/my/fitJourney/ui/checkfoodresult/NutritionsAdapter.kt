package id.my.fitJourney.ui.checkfoodresult

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.my.fitJourney.databinding.ItemNutritionsBinding

class NutritionsAdapter :
    ListAdapter<String, NutritionsAdapter.NutritionsViewHolder>(DIFF_CALLBACK) {

    inner class NutritionsViewHolder(private val binding: ItemNutritionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: String) {
            binding.tvNutritionName.text = result
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NutritionsViewHolder {
        val binding =
            ItemNutritionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NutritionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NutritionsViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}