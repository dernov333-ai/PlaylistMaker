package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class SearchActivity : AppCompatActivity() {
    var searchText: String = ""

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var tracksRecycler: RecyclerView
    private lateinit var emptyPlaceholder: LinearLayout
    private lateinit var errorPlaceholder: LinearLayout
    private lateinit var retryButton: Button
    private lateinit var trackAdapter: TrackAdapter

    private var latestSearchQuery: String = ""

    private val api: ITunesSearchApi = SearchApi.iTunesSearchApi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        searchEditText = findViewById(R.id.etSearch)
        clearButton = findViewById(R.id.ivClear)
        tracksRecycler = findViewById(R.id.rvTrackList)
        emptyPlaceholder = findViewById(R.id.layoutPlaceholderEmpty)
        errorPlaceholder = findViewById(R.id.layoutPlaceholderError)
        retryButton = findViewById(R.id.btnRetry)


        val back: ImageView = findViewById(R.id.iwBack)

        back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })
        trackAdapter = TrackAdapter(emptyList())
        tracksRecycler.layoutManager = LinearLayoutManager(this)
        tracksRecycler.adapter = trackAdapter
        tracksRecycler.visibility = View.GONE


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Заглушка
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s?.toString() ?: ""
                clearButton.visibility = if (searchText.isEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // Заглушка
            }
        })

        clearButton.setOnClickListener {
            searchEditText.setText("")
            searchText = ""
            searchEditText.clearFocus()
            hideKeyboard(searchEditText)
            clearButton.visibility = View.GONE
            trackAdapter.updateTracks(emptyList())
            tracksRecycler.visibility = View.GONE
            emptyPlaceholder.visibility = View.GONE
            errorPlaceholder.visibility = View.GONE
        }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchText.trim()
                if (query.isNotEmpty()) {
                    latestSearchQuery = query
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }

        retryButton.setOnClickListener {
            if (latestSearchQuery.isNotEmpty()) {
                performSearch(latestSearchQuery)
            }
        }


    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущий текст поискового запроса
        outState.putString(KEY_SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Восстанавливаем сохранённый текст и устанавливаем его в EditText
        val restoredText = savedInstanceState.getString(KEY_SEARCH_TEXT, "")
        searchText = restoredText

        val searchEditText: EditText = findViewById(R.id.etSearch)
        searchEditText.setText(restoredText)
        // Кнопка очистки обновится автоматически через TextWatcher
    }
    private fun performSearch(query: String) {
        hideKeyboard(searchEditText)
        emptyPlaceholder.visibility = View.GONE
        errorPlaceholder.visibility = View.GONE

        api.search(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val results = body?.results ?: emptyList()
                    if (results.isEmpty()) {
                        trackAdapter.updateTracks(emptyList())
                        tracksRecycler.visibility = View.GONE
                        emptyPlaceholder.visibility = View.VISIBLE
                        errorPlaceholder.visibility = View.GONE
                    } else {
                        trackAdapter.updateTracks(results)
                        tracksRecycler.visibility = View.VISIBLE
                        emptyPlaceholder.visibility = View.GONE
                        errorPlaceholder.visibility = View.GONE
                    }
                } else {
                    showErrorState()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showErrorState()
            }
        })
    }

    private fun showErrorState() {
        trackAdapter.updateTracks(emptyList())
        tracksRecycler.visibility = View.GONE
        emptyPlaceholder.visibility = View.GONE
        errorPlaceholder.visibility = View.VISIBLE
    }
    companion object {
        private const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
    }


}
