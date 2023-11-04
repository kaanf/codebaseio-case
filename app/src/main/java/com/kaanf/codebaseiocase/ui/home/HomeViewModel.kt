package com.kaanf.codebaseiocase.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaanf.codebaseiocase.data.model.Ad
import com.kaanf.codebaseiocase.data.model.AdList
import com.kaanf.codebaseiocase.domain.usecase.GetAdsUseCase
import com.kaanf.codebaseiocase.ui.AdViewModel
import com.kaanf.codebaseiocase.utils.IOStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAdsUseCase: GetAdsUseCase) : ViewModel(), AdViewModel.Listener {
    var navigator: Navigator? = null

    var isPlaceholderItemsShown: ObservableField<Boolean> = ObservableField(true)

    private val _ads = MutableLiveData<IOStatus<AdList>>()
    val ads: LiveData<IOStatus<AdList>>
        get() = _ads

    private var adViewModels: MutableList<AdViewModel> = mutableListOf()

    fun fetch() {
        getAds()
    }

    private fun getAds() = viewModelScope.launch {
        _ads.value = IOStatus.Loading(isLoading = true)

        delay(3000L)

        try {
            _ads.value = IOStatus.Loading(isLoading = false)

            val result = getAdsUseCase.execute()
            _ads.value = result
        } catch (e: Exception) {
            _ads.value = IOStatus.Failure(e)
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

    fun setVisibilityState(isLoading: Boolean) = viewModelScope.launch {
        isPlaceholderItemsShown.set(isLoading)
    }



    interface Navigator {
        fun onAdClicked(adViewModel: AdViewModel)
    }
}