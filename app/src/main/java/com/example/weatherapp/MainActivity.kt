package com.example.weatherapp

import ApiInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.wheaterapp.R
import com.example.wheaterapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData("islamabad")
        searchcity()
    }

    private fun searchcity() {
      val searchview = binding.searchView2
      searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
          override fun onQueryTextSubmit(query: String?): Boolean {
              if (query != null) {
                  fetchWeatherData(query)
              }
              return true

          }

          override fun onQueryTextChange(newText: String?): Boolean {
return true
          }

      })


    }

    private fun fetchWeatherData(cityname : String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ApiInterface::class.java)

        val call = retrofit.getWeatherData(cityname, "cfd50eb3e7700cd790f5176f752f77f2", "metric")
        call.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                if (response.isSuccessful) {

                    val weatherApp = response.body()
                    val temperature = weatherApp?.main?.temp.toString()
                    val humidty = weatherApp?.main?.humidity.toString()

                    val humidity = weatherApp?.main?.humidity.toString()
                    val clouds = weatherApp?.clouds?.all.toString()
                    val windSpeed = weatherApp?.wind?.speed.toString()
                    val sunrise = weatherApp?.sys?.sunrise?.toLong()
                    val sunset = weatherApp?.sys?.sunset?.toLong()
                    val seal = weatherApp?.main?.pressure
                    val conditions = weatherApp?.weather?.firstOrNull()?.main
                    val maxtemp = weatherApp?.main?.temp_max
                    val mintemp = weatherApp?.main?.temp_min

                  //  val seaLevel = weatherApp?.main?.seaLevel.toString()


                    binding.txttemprture.text = "$temperature ℃"
                    binding.Humidity.text = "$humidity %"
                    binding.rain.text = "$conditions "
                    binding.wind.text = "$windSpeed m/s"
                    binding.textView7.text = "Max Temp : $maxtemp ℃"
                    binding.textView8.text = "Min Temp : $mintemp ℃"
                    binding.sea.text = "$seal hPa"
                    binding.sunrise.text = sunrise?.let { time(it) } ?: "Sunrise: N/A"
                    binding.sunset.text = sunset?.let { time(it) } ?: "Sunset: N/A"

                    binding.textView6.text ="$conditions"
binding.textView9.text =dayname(System.currentTimeMillis())
    binding.textView10.text =date()
    binding.textView2.text ="$cityname"
                    changeimage(conditions.toString())
                 //   binding.sea.text = "Sea Level: $seaLevel hPa"


                 //   Log.e("TAG", "Temperature: $temperature")

                    // Process weatherApp
                } else {
                    Log.e("TAG", "Unsuccessful response: ${response.code()}")
                    // Handle unsuccessful response

                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Log.e("TAG", "Error: ${t.message}", t)
                // Handle failure
            }
        })

    }

    private fun changeimage(conditions: String) {
        when(conditions.toLowerCase(Locale.getDefault())) {
            "clear sky", "sunny", "clear", "fine", "fair", "bright", "sunshine", "sunlit", "radiant", "cloudless" -> {
                binding.root.setBackgroundResource(R.drawable.desert)
                binding.lottieAnimationView2.setAnimation(R.raw.sun)
            }
            "clouds", "partly cloudy", "overcast", "gloomy", "grey", "overcast sky", "hazy", "dreary", "murky", "foggy" -> {
                binding.root.setBackgroundResource(R.drawable.cloudy2)
                binding.lottieAnimationView2.setAnimation(R.raw.cloud)
            }
            "rainy", "showers", "rain", "drizzle", "downpour", "precipitation", "wet", "damp", "rainy weather", "stormy" -> {
                binding.root.setBackgroundResource(R.drawable.rain2)
                binding.lottieAnimationView2.setAnimation(R.raw.rain)
            }
            "snowy", "snow", "blizzard", "snowfall", "snowstorm", "snowy weather", "wintry", "frosty", "icy", "sleet" -> {
                binding.root.setBackgroundResource(R.drawable.snow2)
                binding.lottieAnimationView2.setAnimation(R.raw.snow)
            }
            else -> {
                // Default background and animation if condition doesn't match any of the above
                binding.root.setBackgroundResource(R.drawable.desert)
                binding.lottieAnimationView2.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView2.playAnimation()
    }


    private fun date(): String {
        val sdf =SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun dayname(timestamp: Long): String{
        val sdf =SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
    fun time (timestamp: Long): String{
        val sdf =SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
}
