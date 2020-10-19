package com.app.lurkmoarapp.apimodels


data class ApiSearchResult(
    var searchResults: List<ApiSearchItem> = emptyList()
)

data class ApiSearchItem(
    var id: String = "",
    var name: String = ""
)

