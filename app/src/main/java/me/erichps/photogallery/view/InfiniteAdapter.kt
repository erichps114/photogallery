package me.erichps.photogallery.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.erichps.photogallery.databinding.ItemPhotoListBinding
import me.erichps.photogallery.domain.model.Photo
import javax.inject.Inject

class InfiniteAdapter @Inject constructor(): RecyclerView.Adapter<InfiniteAdapter.PhotoViewHolder>() {

    private val photos = mutableListOf<Photo>()
    inner class PhotoViewHolder(private val binding: ItemPhotoListBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(photo: Photo) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .into(imageContainer)
                username.text = photo.user.name
                description.text = photo.description ?: photo.altDescription
            }

        }
    }

    var onLoadMore: (()-> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoListBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
        if (position == itemCount-2) onLoadMore?.invoke()
    }

    override fun getItemCount(): Int = photos.size

    fun addPhotos(list: List<Photo>) {
        val lastIndex = photos.size
        photos.addAll(list)
        notifyItemRangeInserted(lastIndex, list.size)
    }

    fun clearAll() {
        val size = photos.size
        photos.clear()
        notifyItemRangeRemoved(0,size)
    }
}