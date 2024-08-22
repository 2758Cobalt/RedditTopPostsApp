package com.kolomoiets.reddittoppostsapp.adapters.redditPosts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kolomoiets.reddittoppostsapp.RedditTimeFormatter
import com.kolomoiets.reddittoppostsapp.data.RedditPostData
import com.kolomoiets.reddittoppostsapp.databinding.ItemRedditPostBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class RedditPostListAdapter(private val context: Context): ListAdapter<RedditPostData, RedditPostListAdapter.PostViewHolder>(Comparator()) {
    private var postListData = mutableListOf<RedditPostData>()
    private var timeFormatter = RedditTimeFormatter(context)

    private var picassoInstance = Picasso.Builder(context).build()
    private var redditPostListener: ViewHolderPostListener? = null

    class Comparator: DiffUtil.ItemCallback<RedditPostData>() {
        override fun areItemsTheSame(oldItem: RedditPostData, newItem: RedditPostData): Boolean {
            return oldItem.authorName == newItem.authorName
        }

        override fun areContentsTheSame(oldItem: RedditPostData, newItem: RedditPostData): Boolean {
            return oldItem == newItem
        }
    }

    inner class PostViewHolder(private val itemBinding: ItemRedditPostBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(redditPostData: RedditPostData) {
            itemBinding.apply {
                with(redditPostData) {
                    postId.text = id
                    postAuthorName.text = authorName
                    postTitle.text = title
                    postButtonComments.text = commentsCount.toString()

                    picassoInstance
                        .load(thumbnailUrl)
                        .into(postThumbnail, object : Callback {
                            override fun onSuccess() { postThumbnailCard.isVisible = true }
                            override fun onError(e: Exception?) { }
                        })

                    postCreatedTime.text = timeFormatter.formatTime(createdTime)
                    postThumbnailCard.setOnClickListener { redditPostListener?.onThumbnailClick(thumbnailUrl) }
                }
            }
        }
    }

    // Setup RedditPostListener
    fun setNewListener(listener: ViewHolderPostListener) {
        this.redditPostListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemRedditPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.postThumbnailCard.isVisible = false
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postItem = getItem(position)
        holder.bind(postItem)
    }

    fun addItemListData(newItemList: List<RedditPostData>) {
        for (item in newItemList) {
            postListData.add(item)
            notifyItemInserted(postListData.lastIndex)
        }

        notifyListAdapter()

    }

    private fun notifyListAdapter() {
        this.submitList(postListData)
    }

    fun getLastItemId(): String {
        return postListData[postListData.lastIndex].id
    }

    companion object {
        const val LOG_TAG = "RecyclerViewDebugTag"
    }
}

