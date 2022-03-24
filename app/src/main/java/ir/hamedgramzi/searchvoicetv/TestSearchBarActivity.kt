package ir.hamedgramzi.searchvoicetv

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import ir.hamedgramzi.searchwithvoice.SearchBarHandler
import ir.hamedgramzi.voicelib.SearchBar
import ir.hamedgramzi.voicelib.SearchResultProvider

class TestSearchBarActivity : FragmentActivity() {
    lateinit var searchBarHandler: SearchBarHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_search_bar)


        var searchBar = findViewById<SearchBar>(R.id.search_bar)

        searchBarHandler =
            SearchBarHandler(this, searchBar, object : SearchResultProvider {
                override fun onQueryTextChange(newQuery: String?) {
                    Toast.makeText(
                        this@TestSearchBarActivity,
                        "changed : $newQuery",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onQueryTextSubmit(query: String?) {
                    Toast.makeText(this@TestSearchBarActivity, "submit: $query", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        searchBarHandler.startRecognition()
    }


    override fun onResume() {
        super.onResume()
        searchBarHandler.resume()
    }


    override fun onPause() {
        super.onPause()
//
    }

    override fun onStop() {
        super.onStop()
        searchBarHandler.stop()
    }

}