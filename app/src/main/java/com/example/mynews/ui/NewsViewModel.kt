package com.example.mynews.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mynews.NewsApplication
import com.example.mynews.data.repository.NewsRepository
import com.example.mynews.data.response.Article
import com.example.mynews.data.response.NewsResponse
import com.example.mynews.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app : Application,
    val newsRepository : NewsRepository
) : AndroidViewModel(app) {

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countrycode : String) = viewModelScope.launch {
//        breakingNews.postValue(Resource.Loading())
//        val response = newsRepository.getBreakingNews(countrycode, breakingNewsPage)
//        breakingNews.postValue(handleBreakingNewsResponse(response))
        safeBreakingNewCall(countrycode)
    }

    fun searchNewsFragment(searchQuery: String) = viewModelScope.launch {
//        searchNews.postValue(Resource.Loading())
//        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
//        searchNews.postValue(handleSearchNewsResponse(response))
        safeSearchNewCall(searchQuery)
    }

    private fun handleBreakingNewsResponse( response : Response<NewsResponse>) : Resource<NewsResponse>{
//        if(response.isSuccessful){
//            response.body()?.let { resultResponse ->
//                return Resource.Success(resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
        if(response.isSuccessful){
            response.body()?.let { resultRespone ->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultRespone
                } else {
                    val oldAritccles = breakingNewsResponse?.articles
                    val newArticles = resultRespone.articles
                    oldAritccles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?: resultRespone)
            }

        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse( response : Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultRespone ->
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultRespone
                } else {
                    val oldAritccles = searchNewsResponse?.articles
                    val newArticles = resultRespone.articles
                    oldAritccles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?: resultRespone)
            }

        }
        return Resource.Error(response.message())
    }

    //save article on db and recycle view

    //save on db
    fun saveArticle(article : Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    //get from db
    fun getSaveNews() = newsRepository.getSaveNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    //handle internet
    suspend fun safeBreakingNewCall(countrycode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countrycode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet connection!"))
            }
        } catch (t : Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Convention Error"))
            }
        }
    }

    suspend fun safeSearchNewCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No Internet connection!"))
            }
        } catch (t : Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Convention Error"))
            }
        }
    }

    //check network
    private fun hasInternetConnection() : Boolean {
        val connecttivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connecttivityManager.activeNetwork?: return false
            val capabilities = connecttivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            connecttivityManager.activeNetworkInfo?.run{
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}