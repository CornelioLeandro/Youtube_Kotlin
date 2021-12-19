package com.leandro.youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.video_detail.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val videos = mutableListOf<Video>()
        videoAdapter = VideoAdapter(videos) { video ->
            showOverlayView(video)

        }

        view_layer.alpha = 0f
        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = videoAdapter

        CoroutineScope(Dispatchers.IO).launch {
            val res = async { getVideo() }
            val listVideo = res.await()
            withContext(Dispatchers.Main) {
                listVideo?.let {
                    videos.clear()
                    videos.addAll(listVideo.data)
                    videoAdapter.notifyDataSetChanged()
                    motion_container.removeView(progress_recycler)
                    progress_recycler.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun showOverlayView(video: Video) {
        view_layer.animate().apply {
            duration = 400
            alpha(0.5f)
        }
        motion_container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                println("Transition Trigger  $motionLayout $startId $endId")
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                if (progress > 0.5f)
                    view_layer.alpha = 1.0f - progress
                else
                    view_layer.alpha = 0.5f
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                println("Transition Trigger  $motionLayout $currentId")
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                println("Transition Trigger  $motionLayout $triggerId, $positive $progress ")
            }
        })
    }

    private fun getVideo(): ListVideo? {
        val client = OkHttpClient.Builder()
            .build()

        val request = Request.Builder()
            .get()
            .url("https://tiagoaguiar.co/api/youtube-videos")
            .build()

        return try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                GsonBuilder().create()
                    .fromJson(response.body()?.string(), ListVideo::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

