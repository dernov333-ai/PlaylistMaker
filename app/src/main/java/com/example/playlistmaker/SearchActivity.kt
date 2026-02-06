package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.MockTracks.mockTracks

class SearchActivity : AppCompatActivity() {
    var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchEditText: EditText = findViewById(R.id.etSearch)
        val clearButton: ImageView = findViewById(R.id.ivClear)

        val back: ImageView = findViewById(R.id.iwBack)

        back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

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

        }
        val tracksRecycler: RecyclerView = findViewById(R.id.rvTrackList)

        val trackAdapter = TrackAdapter(mockTracks())
        tracksRecycler.layoutManager = LinearLayoutManager(this)
        tracksRecycler.adapter = trackAdapter
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
    companion object {
        private const val KEY_SEARCH_TEXT = "KEY_SEARCH_TEXT"
    }


}
