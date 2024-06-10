package org.pemmobc1.currencyconvert

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import org.pemmobc1.currencyconvert.Retrofit.RetrofitBuilder
import org.pemmobc1.currencyconvert.Retrofit.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var convert_from: Spinner
    private lateinit var convert_to: Spinner
    private lateinit var currency_converted: EditText
    private lateinit var currency: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        currency_converted = findViewById(R.id.currency_converted)
        currency = findViewById(R.id.currency)
        convert_to = findViewById(R.id.convert_to)
        convert_from = findViewById(R.id.convert_form)
        button = findViewById(R.id.button)

        // Adding functionality
        val dropDownList = arrayOf("USD", "EUR", "IDR", "JPY", "RUB", "CNY")
        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dropDownList)
        convert_to.adapter = adapter
        convert_from.adapter = adapter

        button.setOnClickListener {
            val currencyFrom = convert_from.selectedItem.toString()
            val retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface::class.java)
            val call = retrofitInterface.getExchangeCurrency(currencyFrom)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        val rates = res?.getAsJsonObject("conversion_rates")

                        // Pastikan bahwa properti dalam objek rates sesuai dengan mata uang yang Anda pilih
                        val currencyTo = convert_to.selectedItem.toString()
                        val multiplier = rates?.get(currencyTo)?.asDouble ?: 0.0

                        // Mengambil nilai yang dimasukkan oleh pengguna dan mengonversinya ke tipe data Double
                        val currencyText = currency.text.toString()
                        val currency: Double = currencyText.toDoubleOrNull() ?: run {
                            Log.e("MyTag", "Failed to convert currency input to Double: $currencyText")
                            return
                        }

                        // Menghitung hasil konversi
                        val result = currency * multiplier

                        // Menampilkan hasil konversi di currency_converted
                        currency_converted.setText(result.toString())

                        // Menampilkan log
                        Log.d("MyTag", "Currency: $currency, Multiplier: $multiplier, Result: $result")
                    } else {
                        Log.e("MyTag", "Failed to fetch exchange rates: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Menangani kegagalan permintaan ke server
                    Log.e("MyTag", "Failed to fetch exchange rates: ${t.message}")
                }
            })
        }

    }
}
