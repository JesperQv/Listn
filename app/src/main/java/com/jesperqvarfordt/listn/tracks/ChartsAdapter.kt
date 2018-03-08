package com.jesperqvarfordt.listn.tracks

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.domain.model.Chart
import kotlinx.android.synthetic.main.item_chart.view.*

class ChartsAdapter
constructor(private val chartClicked: (chart: Chart) -> Unit) :
        RecyclerView.Adapter<ChartsAdapter.ChartViewHolder>() {

    private var charts = mutableListOf<Chart>()

    fun updateCharts(charts: List<Chart>) {
        this.charts = charts.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChartsAdapter.ChartViewHolder{
        return ChartViewHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_chart, parent, false))
    }

    override fun onBindViewHolder(holder: ChartsAdapter.ChartViewHolder?, position: Int) {
        holder?.onBind(charts[position])
    }

    override fun getItemCount(): Int {
        return charts.size
    }

    inner class ChartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun onBind(chart: Chart) {
            itemView.name.text = chart.name
            itemView.chartImage.setImageURI(Uri.parse(chart.imageUri))
            itemView.setOnClickListener { chartClicked.invoke(chart) }
        }

    }
}