package com.waynemutai.cryptotracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://api.nomics.com/v1/currencies/ticker"
    private val API_KEY = "37d35dfe5076942b2a54d569f7d6d989"
    private val INTERVAL = "1d"
    private val CONVERT = "KES"

    //member Variable
    private var mPriceTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPriceTextView = findViewById(R.id.PriceLabel)
        val spinner = findViewById<Spinner>(R.id.Currency_spinner)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.currency_array, R.layout.spinner_item
        )

        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown)

        // Apply the adapter to the spinner
        spinner.adapter = adapter

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    Log.d("Crypto", "${parent.getItemAtPosition(position)}")

                    val params = RequestParams()
                    params.put("key", API_KEY)
                    params.put("interval", INTERVAL)
                    params.put("convert", CONVERT)
                    params.put("ids", parent.getItemAtPosition(position).toString())
                    params.letsDoSomeNetworking()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Crypto", "Nothing Selected!")
            }
        }
    }

    // TODO: complete the letsDoSomeNetworking() method
    private fun RequestParams.letsDoSomeNetworking(){
        val client = AsyncHttpClient()
        client.get(BASE_URL, this, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?){
//                if (response != null) {
//                    val price =  response.getJSONObject(0).getString("price")
//                    Log.d("Crypto", price)
//                }
                val currencyPrice = response?.getJSONObject(0)?.getString("price")?.toDouble()
                mPriceTextView?.text = "%.2f KES".format(currencyPrice)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                throwable: Throwable?,
                errorResponse: JSONArray?
            ) {
                Log.d("Crypto", "onFailure: Nothing")
            }
        })
    }
}