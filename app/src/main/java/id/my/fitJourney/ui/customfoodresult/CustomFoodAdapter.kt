package id.my.fitJourney.ui.customfoodresult

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.my.fitJourney.data.remote.response.CustomFoodResponseItem
import id.my.fitJourney.R
import id.my.fitJourney.databinding.ItemResultBinding


class CustomFoodAdapter :
    ListAdapter<CustomFoodResponseItem, CustomFoodAdapter.CustomFoodViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var isFavoriteMap: Map<String, Boolean> = emptyMap()

    inner class CustomFoodViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: CustomFoodResponseItem, clickListener: OnItemClickCallback) {
            binding.tvFoodName.text = result.name
            Glide.with(binding.cvResultItem.context)
                .load(result.images)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(binding.ivFoodResult)

            if (isFavoriteMap[result.name] == true) {
                binding.ibFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.ibFavorite.context,
                        R.drawable.baseline_favorite_24
                    )
                )
            } else {
                binding.ibFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.ibFavorite.context,
                        R.drawable.baseline_favorite_border_24
                    )
                )
            }

            binding.cvResultItem.setOnClickListener {
                clickListener.onItemClicked(result)
            }
            binding.ibFavorite.setOnClickListener {
                clickListener.onFavoriteClicked(result, binding.ibFavorite)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomFoodViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomFoodViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result, onItemClickCallback)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setIsFavoriteMap(isFavoriteMap: Map<String, Boolean>) {
        this.isFavoriteMap = isFavoriteMap
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: CustomFoodResponseItem)
        fun onFavoriteClicked(data: CustomFoodResponseItem, btnFavorite: ImageButton)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CustomFoodResponseItem>() {
            override fun areItemsTheSame(
                oldItem: CustomFoodResponseItem,
                newItem: CustomFoodResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CustomFoodResponseItem,
                newItem: CustomFoodResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}