package com.kolomoiets.reddittoppostsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cobaltumapps.reddittoppostsappblank.retrofit.posts.data.PostResponse
import com.kolomoiets.reddittoppostsapp.adapters.redditPosts.RedditPostListAdapter
import com.kolomoiets.reddittoppostsapp.adapters.redditPosts.ViewHolderPostListener
import com.kolomoiets.reddittoppostsapp.data.RedditPostData
import com.kolomoiets.reddittoppostsapp.databinding.ActivityMainBinding
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.RedditPostParams
import com.kolomoiets.reddittoppostsapp.retrofit.posts.data.subdata.RedditApiTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ViewHolderPostListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var postAdapter: RedditPostListAdapter
    private lateinit var postManager: RedditManager

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

                if (!recyclerView.canScrollVertically(1)) {
                    Log.i(LOG_TAG, postAdapter.getLastItemId())
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


    companion object {
        const val LOG_TAG = "MainActivityTag"
    }
}