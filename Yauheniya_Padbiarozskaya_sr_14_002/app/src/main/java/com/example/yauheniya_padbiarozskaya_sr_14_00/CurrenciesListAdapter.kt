package com.example.yauheniya_padbiarozskaya_sr_14_00

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class CurrenciesListAdapter(var dataSet: MutableList<Currency>, val context: Context) :
    RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currencyName: TextView = view.findViewById(R.id.currencyName)
        val currencyValue: TextView = view.findViewById(R.id.currencyValue)
        val currencyFlag: ImageView = view.findViewById(R.id.currencyFlag)
        val riseLabel: ImageView = view.findViewById(R.id.riseLabell)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.currency_row, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val df = DecimalFormat("0")
        df.maximumFractionDigits = 340;

        val currency = dataSet[position]
        viewHolder.currencyName.text = currency.currencyName
        viewHolder.currencyValue.text = df.format(currency.curValue)
        viewHolder.currencyFlag.setImageResource(currency.curFlag)
        if (currency.rise == 1)
            viewHolder.riseLabel.setImageResource(R.drawable.green)
        else if (currency.rise == 2)
            viewHolder.riseLabel.setImageResource(R.drawable.red_arr)
        else
            viewHolder.riseLabel.setImageResource(R.drawable.equals)


        viewHolder.itemView.setOnClickListener{goToDetailsActivity(currency.currencyName,
        currency.table)}
    }

    private fun goToDetailsActivity(currencyCode: String, table: String){
        val intent = Intent(context, CurDetailsActivity::class.java).apply {
            putExtra("currencyCode", currencyCode)
            putExtra("table", table)
        }
        context.startActivity(intent)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
