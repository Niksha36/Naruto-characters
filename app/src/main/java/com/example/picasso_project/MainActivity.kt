package com.example.picasso_project
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if(counter < totalNinjas) {
                counter ++
                getResult(counter)
        } else {
                binding.textView.visibility = View.VISIBLE
                binding.textView.text = "Персонажи кончились"
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
                    Picasso.get().load(imageURL).error(R.drawable.cross_error).into(binding.imageView)
                    binding.textView.visibility = View.VISIBLE
                    binding.textView.text = name.toString()
                } else {
                    binding.imageView.setImageResource(R.drawable.cross_error)
                }
            },
            {
                binding.imageView.setImageResource(R.drawable.cross_error)
            }
        )
        queue.add(stringRequest)
    }
}