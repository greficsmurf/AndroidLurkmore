package com.app.lurkmoarapp.apimodels

import com.app.lurkmoarapp.api.qualifiers.ApiSectionTitle
import com.squareup.moshi.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class LurkApiAdapter{

    @FromJson
    fun fromJson(jsonReader: JsonReader): ApiSearchResult {

        val list = mutableListOf<ApiSearchItem>()
        if(jsonReader.hasNext())
        {
            var token = jsonReader.peek()
            if(token == JsonReader.Token.BEGIN_ARRAY){
                jsonReader.beginArray()

                while (jsonReader.hasNext()){
                    token = jsonReader.peek()

                    if(token == JsonReader.Token.BEGIN_ARRAY){

                        val response = jsonReader.readJsonValue()
                        response?.let {
                            if(it is ArrayList<*>)
                                it.forEach {str ->
                                    if(str is String)
                                        list.add(ApiSearchItem(str, str))
                                }
                        }

                        
                    }
                    else
                        jsonReader.readJsonValue()
                }
                jsonReader.endArray()
            }
        }

        return ApiSearchResult(list)
    }
    @ToJson
    fun toJson(writer: JsonWriter, value: ApiSearchResult?) {
    }

}


