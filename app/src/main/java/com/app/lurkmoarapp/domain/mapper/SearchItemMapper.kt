package com.app.lurkmoarapp.domain.mapper

import com.app.lurkmoarapp.apimodels.ApiSearchItem
import com.app.lurkmoarapp.apimodels.ApiSearchResult
import com.app.lurkmoarapp.db.model.DbSearchItem
import com.app.lurkmoarapp.domain.SearchItem
import com.app.lurkmoarapp.domain.SearchResult

fun DbSearchItem.asDomainModel(): SearchItem{
    return SearchItem(
        title
    )
}

// SearchResult
fun ApiSearchResult.asDomainModel(): SearchResult{
    return SearchResult(
        searchResults.map {
            it.asDomainModel()
        }
    )
}


//  SearchItem
fun ApiSearchItem.asDomainModel(): SearchItem{
    return SearchItem(
        name
    )
}
fun List<DbSearchItem>.asDomainModel() = SearchResult(
    map {
        it.asDomainModel()
    }
)
fun SearchItem.asDbModel(): DbSearchItem{
    return DbSearchItem(
        title
    )
}
fun SearchItem.asApiModel(): ApiSearchItem{
    return ApiSearchItem(
        name = title
    )
}