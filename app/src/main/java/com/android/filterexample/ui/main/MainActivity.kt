package com.android.filterexample.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.filterexample.R.layout
import com.android.filterexample.model.Image
import com.android.filterexample.ui.adapter.FiltersAdapter
import com.android.filterexample.ui.adapter.ImagesAdapter
import com.android.filterexample.ui.listeners.FilterClickListener
import kotlinx.android.synthetic.main.activity_main.filterLayout
import kotlinx.android.synthetic.main.activity_main.iv_filter
import kotlinx.android.synthetic.main.activity_main.rv_filter
import kotlinx.android.synthetic.main.activity_main.rv_images
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.tv_apply_filter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity(),
    FilterClickListener {

    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var filtersAdapter: FiltersAdapter
    private val selectedFilters = arrayListOf<String>()

    private val filters = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)
        initFiltersList()
        setupFiltersUI()
        setUpImagesUI()
        readFile()?.let {
            parseJSON(it)
        }
        initListeners()
    }

    private fun setUpImagesUI() {
        rv_images.layoutManager = GridLayoutManager(this, 3)
        imagesAdapter = ImagesAdapter(arrayListOf(), arrayListOf())
        rv_images.adapter = imagesAdapter
    }

    private fun initFiltersList() {
        filters.apply {
            add("Dogs")
            add("Cats")
            add("Coffee Quotes")
            add("Inspirational Quotes")
        }
    }

    private fun readFile(): String? {
        val json: String?
        try {
            val inputStream: InputStream =
                assets.open("Response.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun parseJSON(json: String) {
        try {
            val images = arrayListOf<Image>()
            val obj = JSONObject(json)
            val jsonArray: JSONArray = obj.getJSONArray("results")
            for (i in 0 until jsonArray.length()) {
                (jsonArray.get(i) as JSONObject).let { jsonObject ->
                    val image = Image(
                        category = jsonObject.getString("category"),
                        url = jsonObject.getString("url")
                    )
                    images.add(image)
                }
            }
            setImages(images)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun initListeners() {
        iv_filter.setOnClickListener {
            filterLayout.visibility = View.VISIBLE
        }
        tv_apply_filter.setOnClickListener {
            Toast.makeText(this, "Filter Applied!", Toast.LENGTH_SHORT).show()
            setSelectedImages(imagesAdapter.filterImages(selectedFilters))
            filterLayout.visibility = View.GONE
        }
    }

    private fun setImages(images: List<Image>) {
        imagesAdapter.apply {
            addImages(images)
            notifyDataSetChanged()
        }
    }

    private fun setSelectedImages(images: List<Image>) {
        imagesAdapter.apply {
            addSelectedImages(images)
            notifyDataSetChanged()
        }
    }

    private fun setupFiltersUI() {
        rv_filter.layoutManager = GridLayoutManager(this, 2)
        filtersAdapter = FiltersAdapter(filters, this)
        rv_filter.adapter = filtersAdapter
    }

    override fun onFilterItemClicked(filter: String, isSelected: Boolean) {
        if (isSelected)
            selectedFilters.add(filter)
        else {
            if (selectedFilters.contains(filter)) {
                selectedFilters.remove(filter)
            }
        }
    }
}
