package com.kaanf.codebaseiocase.domain.usecase

import com.kaanf.codebaseiocase.data.model.AdList
import com.kaanf.codebaseiocase.domain.repository.HomeRepository
import com.kaanf.codebaseiocase.utils.IOStatus
import javax.inject.Inject

class GetAdsUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun execute(): IOStatus<AdList> = homeRepository.fetchAds()
}