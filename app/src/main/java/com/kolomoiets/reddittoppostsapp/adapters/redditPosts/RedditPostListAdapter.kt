package com.kolomoiets.reddittoppostsapp.adapters.redditPosts

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cobaltumapps.reddittoppostsappblank.retrofit.posts.data.PostResponse
import com.kolomoiets.reddittoppostsapp.RedditTimeFormatter
import com.kolomoiets.reddittoppostsapp.data.RedditPostData
import com.kolomoiets.reddittoppostsapp.databinding.ItemRedditPostBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

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

                            override fun onSuccess() {
                                postThumbnailCard.isVisible = true
                                postButtonSaveToGallery.isVisible = true
                            }
                            override fun onError(e: Exception?) {
                                postThumbnailCard.isVisible = false
                                postButtonSaveToGallery.isVisible = false
                            }
                        })



                    postCreatedTime.text = timeFormatter.formatTime(createdTime)
                    postThumbnailCard.setOnClickListener { redditPostListener?.onThumbnailClick(thumbnailUrl) }
                    postButtonSaveToGallery.setOnClickListener { redditPostListener?.actionSaveToGallery(thumbnailUrl)}
                }
            }
        }
    }

    // Setup ViewHolderPostListener
    fun setNewListener(listener: ViewHolderPostListener) {
        this.redditPostListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemRedditPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.postThumbnailCard.isVisible = false
        binding.postButtonSaveToGallery.isVisible = false
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postItem = getItem(position)
        holder.bind(postItem)
    }

    fun setNewItemsList(newList: MutableList<RedditPostData>) {
        postListData = newList
        notifyListAdapter()
    }
    fun addItemToListAdapter(postResponse: PostResponse) {
        for (item in postResponse.data.children) {
            postListData.add(
                RedditPostData(
                    item.data.name,
                    item.data.subreddit_name_prefixed,
                    item.data.title,
                    item.data.created_utc,
                    item.data.thumbnail,
                    item.data.num_comments
                )
            )
            notifyItemInserted(postListData.lastIndex)
        }

        notifyListAdapter()

    }

    private fun notifyListAdapter() {
        this.submitList(postListData)
    }

    fun getList(): MutableList<RedditPostData> {
        return postListData
    }

    fun getLastItemId(): String {
        return postListData[postListData.lastIndex].id
    }

    companion object {
        const val LOG_TAG = "RecyclerViewDebugTag"
    }
}

