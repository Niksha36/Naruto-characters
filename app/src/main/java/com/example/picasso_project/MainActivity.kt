package com.example.picasso_project
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.picasso_project.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var counter: Int = -1
    private var totalNinjas = 0
    private val adapter = Adapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if(counter < totalNinjas) {
                counter ++
                getResult(counter)
        } else {
                binding.button.visibility = View.GONE
            }

            }
    }

    private fun getResult(counter:Int){
        val url = "https://narutodb.xyz/api/character?limit=100"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET,
            url,
            {
                response->
                val obj = JSONObject(response)
                val characters = obj.getJSONArray("characters")
                totalNinjas = characters.length() - 1
                val character = characters.getJSONObject(counter) // Access the first item in the array
                val imagesCount = character.getJSONArray("images").length()
                val name = character.getString("name")

                if(imagesCount>0) {
                    val imageURL = character.getJSONArray("images").getString(0)
                    binding.recyclerCharacter.layoutManager = GridLayoutManager(this@MainActivity, 3)
                    binding.recyclerCharacter.adapter = adapter
                    val personData = Character(name, imageURL)
                    adapter.addCharacter(personData)
                }
            },
            {
//
            }
        )
        queue.add(stringRequest)
    }
}