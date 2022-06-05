package com.example.listtest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameView : AppCompatActivity() {
    val jsonConverter: GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().create())
    val retrofitClient: Retrofit = Retrofit.Builder()
        .baseUrl(MainActivity.baseURL)
        .addConverterFactory(jsonConverter)
        .build()
    val service: GameAPI = retrofitClient.create(GameAPI::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_view)
        val id = intent.getIntExtra("id", 1)
        service.getDetails(id).enqueue(gameDetailsCallback)
    }


    val gameDetailsCallback: Callback<GameDetails> = object : Callback<GameDetails> {
        override fun onFailure(call: Call<GameDetails>, t: Throwable) {
            // Code here what happens if calling the WebService fails
            Log.w(MainActivity.TAG, "WebService call failed")
        }

        override fun onResponse(
            call: Call<GameDetails>, response:
            Response<GameDetails>
        ) {
            if (response.code() == 200) {
                // We got our data !
                val responseData = response.body()
                if (responseData != null) {
                    findViewById<TextView>(R.id.name_value).text = responseData.name
                    findViewById<TextView>(R.id.type_value).text = responseData.type
                    findViewById<TextView>(R.id.nb_value).text = responseData.players.toString()
                    findViewById<TextView>(R.id.year_value).text = responseData.year.toString()
                    findViewById<TextView>(R.id.description).text = responseData.description_en
                    Glide
                        .with(this@GameView)
                        .load(responseData.picture)
                        .into(findViewById(R.id.icon_value))
                    findViewById<Button>(R.id.button).setOnClickListener {
                        // Define an implicit intent
                        val implicitIntent = Intent(Intent.ACTION_VIEW)
                        // Add the required data in the intent (here the URL we want to open)
                        implicitIntent.data = Uri.parse(responseData.url)
                        // Launch the intent
                        startActivity(implicitIntent)
                    }

                }
            }
        }
    }
}