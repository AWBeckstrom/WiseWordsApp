package com.example.quotesapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity
import com.example.quotesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getQuote()

        binding.nextBtn.setOnClickListener {
            getQuote()
        }
   }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getQuote() {
        setInProgress(true)

        GlobalScope.launch {
            try {
                val response = RetrofitInstance.quoteApi.getRandomQuote()
                Log.d("API Response", "Response:${response.body()}")
                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let {
                        setUI(it)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    setInProgress(false)
                    Toast.makeText(applicationContext,"Check Internet Connection", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
    private fun setUI(quote: QuoteModel) {
        binding.quoteTv.text = quote.q
        binding.authorTv.text = quote.a
    }

    private fun setInProgress(inProgress: Boolean) {
       if (inProgress) {
          binding.progressBar.visibility = View.VISIBLE
          binding.nextBtn.visibility = View.GONE
       } else {
          binding.progressBar.visibility = View.GONE
          binding.nextBtn.visibility = View.VISIBLE
       }
    }
}


