package com.powerhouseai.myweather.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel
import com.powerhouseai.myweather.databinding.ItemCitiesWeatherBinding

class WeatherAdapter(private val itemClick: (WeatherUiModel) -> Unit) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<WeatherUiModel>() {
        override fun areItemsTheSame(oldItem: WeatherUiModel, newItem: WeatherUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherUiModel, newItem: WeatherUiModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<WeatherUiModel>?) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemCitiesWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding, itemClick)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherAdapter.WeatherViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class WeatherViewHolder(
        private val binding: ItemCitiesWeatherBinding,
        private val itemClick: (WeatherUiModel) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: WeatherUiModel) {
            with(binding) {
                itemView.setOnClickListener {
                    itemClick(item)
                }

                Glide.with(itemView.context)
                    .load(item.weatherIcon)
                    .centerCrop()
                    .into(ivWeather)
                ivWeather.contentDescription = item.weatherDescription

                tvTemperature.text = item.temperature
                tvCityName.text = item.currentCity
                tvCountryCode.text = item.countryCode
                tvHumidity.text = item.humidity
                tvWind.text = item.wind
            }
        }
    }
}