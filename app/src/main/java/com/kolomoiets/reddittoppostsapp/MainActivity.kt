package com.kolomoiets.reddittoppostsapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kolomoiets.reddittoppostsapp.adapters.redditPosts.RedditPostListAdapter
import com.kolomoiets.reddittoppostsapp.adapters.redditPosts.ViewHolderPostListener
import com.kolomoiets.reddittoppostsapp.data.RedditPostData
import com.kolomoiets.reddittoppostsapp.databinding.ActivityMainBinding
import com.kolomoiets.reddittoppostsapp.imageSaver.PicassoImageSaver
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.RedditPostParams
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.subdata.RedditApiTime
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class MainActivity : AppCompatActivity(), ViewHolderPostListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var postAdapter: RedditPostListAdapter
    private lateinit var postManager: RedditManager

    private var picassoImageSaver = PicassoImageSaver(this)

    private val LIST_STATE_KEY = "reddit_post_list_state"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.main_actionBar_title)

        postManager = RedditManager()
        postAdapter = RedditPostListAdapter(this)

        postAdapter.setNewListener(this)

        binding.recyclerViewMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }

        binding.recyclerViewMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && postAdapter.getList().isNotEmpty()) {
                    loadData(
                        RedditPostParams(
                        afterNameId = postAdapter.getLastItemId(),
                        time = RedditApiTime.week
                    )
                    )
                }
            }
        })

        if (savedInstanceState == null) {
            loadData()
        }
        // Завантаження збереженого списку елементів і застовування їх у RecyclerView
        else {
            val savedRedditPosts =  savedInstanceState.getParcelableArrayList<RedditPostData>(LIST_STATE_KEY)?.toMutableList() ?: mutableListOf<RedditPostData>()
            postAdapter.setNewItemsList(savedRedditPosts)
        }

    }

    // Збереження списку елементів у RecyclerView
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val redditPostsList = postAdapter.getList()
        outState.putParcelableArrayList(LIST_STATE_KEY, ArrayList(redditPostsList))
    }

    private fun loadData(redditPostConfig: RedditPostParams = RedditPostParams()) {
        binding.updatingProgressMain.apply {
            isVisible = true
            isIndeterminate = true
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = postManager.getTopPosts(redditPostConfig)

            runOnUiThread {
                postAdapter.addItemToListAdapter(response!!)

                binding.updatingProgressMain.apply {
                    isVisible = false
                    isIndeterminate = false
                }
            }

        }
    }

    // Відкриває картинку у браузері
    override fun onThumbnailClick(thumbnailUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(thumbnailUrl)
        }
        startActivity(intent)
    }

    override fun actionSaveToGallery(thumbnailUrl: String) {
        picassoImageSaver.saveImageToGallery(thumbnailUrl)
    }

    companion object {
        const val LOG_TAG = "MainActivityTag"
    }
}