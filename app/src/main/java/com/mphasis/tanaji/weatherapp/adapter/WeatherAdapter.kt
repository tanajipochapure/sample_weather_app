package com.mphasis.tanaji.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mphasis.tanaji.weatherapp.R
import com.mphasis.tanaji.weatherapp.model.CityWiseModelNew

class WeatherAdapter(
    private val context: Context,
    private val cityWiseList: List<CityWiseModelNew.Weather>
) :
    RecyclerView.Adapter<WeatherAdapter.Holder>() {

    inner class Holder(view: View) : ViewHolder(view) {
        val dateTv: TextView = view.findViewById(R.id.dateTv)
        val tempTv: TextView = view.findViewById(R.id.tempTv)
        val sunRiseTimeTv: TextView = view.findViewById(R.id.sunRiseTimeTv)
        val sunSetTimeTv: TextView = view.findViewById(R.id.sunSetTimeTv)
        val sunRiseIv: ImageView = view.findViewById(R.id.sunRiseIv)
        val sunSetIv: ImageView = view.findViewById(R.id.sunSetIv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.list_row_day, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return cityWiseList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val weather = cityWiseList[position]
        holder.dateTv.text = weather.date
        holder.tempTv.text = "${weather.maxtempC}°"
        for (i in weather.astronomy) {
            holder.sunRiseTimeTv.text = i.sunrise
            holder.sunSetTimeTv.text = i.sunset
        }

        /* Glide.with(context).load("https://openweathermap.org/img/wn/02d.png")
             .placeholder(R.drawable.outline_wb_sunny_24)
             .apply(RequestOptions.skipMemoryCacheOf(true))
             .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
             .dontAnimate()
             .into(holder.sunRiseIv)

         Glide.with(context).load("https://openweathermap.org/img/wn/02n.png")
             .placeholder(R.drawable.outline_wb_sunny_24)
             .apply(RequestOptions.skipMemoryCacheOf(true))
             .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
             .dontAnimate()
             .into(holder.sunSetIv)*/
    }
}