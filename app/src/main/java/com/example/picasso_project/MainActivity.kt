package com.example.picasso_project
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.picasso_project.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var counter: Int = 1
    private val adapter = Adapter()
    private val requestQueue by lazy { Volley.newRequestQueue(this) } // Use lazy initialization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadMoreCharacters()

        binding.button.setOnClickListener {
            loadMoreCharacters()
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerCharacter) {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = this@MainActivity.adapter
        }
    }

    private fun loadMoreCharacters() {
        if(counter >= 100) {
            binding.button.visibility = View.GONE
            return
        }
        val limit = 9 // Adjust based on how many items you want to fetch each time
        val url = "https://narutodb.xyz/api/character?page=$counter&limit=$limit"
        binding.progressBar.visibility = View.VISIBLE
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            handleResponse(response)
            binding.progressBar.visibility = View.GONE
        }, {
            // Handle error
        })
        GlobalScope.launch(Dispatchers.IO) {
            requestQueue.add(stringRequest)
        }
    }

    private fun handleResponse(response: String) {
        val obj = JSONObject(response)
        val characters = obj.getJSONArray("characters")
        val newCharacters = mutableListOf<Character>()

        for (i in 0 until characters.length()) {
            val character = characters.getJSONObject(i)
            val imagesCount = character.getJSONArray("images").length()
            if(imagesCount > 0) {
                val name = character.getString("name")
                val imageURL = character.getJSONArray("images").getString(0)
                newCharacters.add(Character(name, imageURL))
            }
        }

        adapter.addCharacter(newCharacters)
        counter += 1
    }
}