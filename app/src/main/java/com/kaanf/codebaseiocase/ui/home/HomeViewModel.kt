package com.kaanf.codebaseiocase.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaanf.codebaseiocase.data.model.Ad
import com.kaanf.codebaseiocase.data.model.AdList
import com.kaanf.codebaseiocase.domain.usecase.GetAdsUseCase
import com.kaanf.codebaseiocase.ui.home.item.AdViewModel
import com.kaanf.codebaseiocase.utils.IOStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAdsUseCase: GetAdsUseCase) : ViewModel(), AdViewModel.Listener {
    var navigator: Navigator? = null

    var isPlaceholderItemsShown: ObservableField<Boolean> = ObservableField(true)
    var isClearIconShown: ObservableField<Boolean> = ObservableField(false)
    var isErrorShown: ObservableField<Boolean> = ObservableField(false)

    var searchQuery: ObservableField<String> = ObservableField()

    private val _ads = MutableLiveData<IOStatus<AdList>>()
    val ads: LiveData<IOStatus<AdList>>
        get() = _ads

    var adViewModels: MutableList<AdViewModel> = mutableListOf()

    fun fetch() {
        getAds()
    }

    private fun getAds() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _ads.postValue(IOStatus.Loading(isLoading = true))

            delay(3000L)

            try {
                _ads.postValue(IOStatus.Loading(isLoading = false))

                getAdsUseCase.execute().also {
                    _ads.postValue(it)
                }
            } catch (e: Exception) {
                _ads.postValue(IOStatus.Failure(e))
            }
        }
    }

    fun getViewModels(ads: AdList): List<AdViewModel> {
        val adViewModels: MutableList<AdViewModel> = mutableListOf()

        ads.data.forEach { ad ->
            adViewModels.add(AdViewModel(listener = this, ad = ad))
        }

        this.adViewModels = adViewModels

        return adViewModels
    }

    override fun onAdClicked(ad: Ad) {
        adViewModels.forEach {
            if (it.ad == ad)
                navigator?.onAdClicked(adViewModel = it)
        }
    }

    fun setPlaceholderVisibility(isVisible: Boolean) {
        isPlaceholderItemsShown.set(isVisible)
    }

    fun setClearIconVisibility(isVisible: Boolean) {
        isClearIconShown.set(isVisible)
    }

    fun updateSearchQuery(query: String) {
        searchQuery.set(query)
    }

    fun onClearClicked() {
        searchQuery.set("")
    }

    fun onError(isErrorVisible: Boolean) {
        isErrorShown.set(isErrorVisible)
        isPlaceholderItemsShown.set(!isErrorVisible)
    }

    interface Navigator {
        fun onAdClicked(adViewModel: AdViewModel)
    }
}