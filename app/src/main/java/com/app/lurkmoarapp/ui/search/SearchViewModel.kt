package com.app.lurkmoarapp.ui.search

import androidx.lifecycle.*
import com.app.lurkmoarapp.apimodels.ApiSearchResult
import com.app.lurkmoarapp.domain.SearchItem
import com.app.lurkmoarapp.domain.SearchResult
import com.app.lurkmoarapp.repo.SearchRepo
import com.app.lurkmoarapp.vo.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repo: SearchRepo
) : ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val _searchString = MutableLiveData<String>()
    val searchString: LiveData<String>
        get() = _searchString

    private val _isHistory = MutableLiveData<Boolean>()
    val isHistory: LiveData<Boolean>
         get() = _isHistory

    private val searchHistory = repo.getLastSearchItemsResource(10).asLiveData()

    val searchResultResource: LiveData<Resource<out SearchResult>> = searchString.switchMap{
        if (it.isNotBlank())
        {
            _isHistory.value = false
            return@switchMap repo.getSearchResultsResource(it).asLiveData()
        }
        else
        {
            _isHistory.value = true
            return@switchMap searchHistory
        }
    }

//    private val searchHistory = repo.getLastSearchItems(10).asLiveData()
//
//    val searchResult: LiveData<SearchResult> = searchString.switchMap{
//        _isLoading.value = true
//        if (it.isNotBlank())
//        {
//            viewModelScope.launch {
//                repo.insertSearchItem(
//                    SearchItem(
//                        it
//                    )
//                )
//            }
//
//            liveData {
//                emit(repo.getSearchResults(it))
//                _isHistory.postValue(false)
//            }
//        }
//        else
//        {
//            _isHistory.value = true
//            return@switchMap searchHistory
//        }
//    }

    fun deleteAllHistory(){
        viewModelScope.launch {
            repo.deleteAllHistory()
        }
    }

    init {
        Timber.i("Created")
    }


    fun setSearchString(searchStr: String?){
        if(searchStr != _searchString.value)
            _searchString.postValue(searchStr)
    }

    override fun onCleared() {
        Timber.d("SearchViewModel OnDestroy")

        super.onCleared()
        job.cancel()
    }
}