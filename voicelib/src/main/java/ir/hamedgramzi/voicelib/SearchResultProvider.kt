package ir.hamedgramzi.voicelib

interface SearchResultProvider {
    fun onQueryTextChange(newQuery: String?)
    fun onQueryTextSubmit(query: String?)
    fun onQueryComplete() {}
    fun onVoiceButtonPress(){}
}