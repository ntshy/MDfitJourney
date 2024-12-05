package id.my.fitJourney.ui.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.my.fitJourney.data.local.entity.FoodData
import id.my.fitJourney.R
import id.my.fitJourney.databinding.FavoriteItemBinding

class FavoriteAdapter :
    ListAdapter<FoodData, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class FavoriteViewHolder(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: FoodData, clickListener: OnItemClickCallback) {
            Log.d("FavoriteAdapter", "Binding item: ${result.name}")
            binding.tvFoodName.text = result.name
            Glide.with(binding.ivFavoriteFood.context)
                .load(result.images)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(binding.ivFavoriteFood)

            binding.tvFoodDesc.text = result.description

            binding.ibFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ibFavorite.context,
                    R.drawable.baseline_favorite_24
                )
            )

            binding.itemFavorite.setOnClickListener {
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
    ): FavoriteViewHolder {
        val binding =
            FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result, onItemClickCallback)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun setIsFavoriteMap(isFavoriteMap: Map<String, Boolean>) {
//        this.isFavoriteMap = isFavoriteMap
//        notifyDataSetChanged()
//    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FoodData)
        fun onFavoriteClicked(data: FoodData, btnFavorite: ImageButton)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodData>() {
            override fun areItemsTheSame(oldItem: FoodData, newItem: FoodData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FoodData, newItem: FoodData): Boolean {
                return oldItem == newItem
            }
        }
    }
}