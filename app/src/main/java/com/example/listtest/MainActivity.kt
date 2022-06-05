package com.example.listtest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object
    {
        const val baseURL = "https://education.3ie.fr/android/ressources/api/game/"
        const val TAG = "LISTS"
    }

    val jsonConverter : GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().create())
    val retrofitClient : Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(jsonConverter)
        .build()
    val service: GameAPI = retrofitClient.create(GameAPI::class.java)
    lateinit var recyclerView : RecyclerView;


    var games : MutableList<GameObject> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = findViewById<RecyclerView>(R.id.game_view)
        recyclerView = list
// display performance optimization when list widget size does not change
        list.setHasFixedSize(true)
// here we specify this is a standard vertical list
        list.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false)
        // attach an adapter and provide some data
        list.adapter = CustomRecyclerAdapter(this@MainActivity, games) {
            Log.d(TAG, "Clicked the row")
            val explicitIntent = Intent(this@MainActivity, GameView::class.java)
            startActivity(explicitIntent)
        }
        // Add separator
        list.addItemDecoration(
            DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
        )
        service.listGames().enqueue(gameObjectCallBack);
    }

    val gameObjectCallBack: Callback<List<GameObject>> = object : Callback<List<GameObject>> {
        override fun onFailure(call: Call<List<GameObject>>, t: Throwable) {
            // Code here what happens if calling the WebService fails
            Log.w(TAG, "WebService call failed")
        }

        override fun onResponse(
            call: Call<List<GameObject>>, response:
            Response<List<GameObject>>
        ) {
            if (response.code() == 200) {
                // We got our data !
                val responseData = response.body()
                if (responseData != null) {
                    games.addAll(0, responseData)
                    // attach an adapter and provide some data
                    recyclerView.adapter = CustomRecyclerAdapter(this@MainActivity, games)  {
                        it.tag
                        Log.d(TAG, "Clicked the row")
                        val explicitIntent = Intent(this@MainActivity, GameView::class.java)
                        explicitIntent.putExtra("id", it.tag as Int)
                        startActivity(explicitIntent)
                    }
                    // Add separator
                    recyclerView.addItemDecoration(
                        DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
                    )
                }
            }
        }
    }
}