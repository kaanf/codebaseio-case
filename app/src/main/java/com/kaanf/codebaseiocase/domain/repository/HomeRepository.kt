package com.kaanf.codebaseiocase.domain.repository

import com.kaanf.codebaseiocase.data.model.AdList
import com.kaanf.codebaseiocase.utils.IOStatus

interface HomeRepository {
    suspend fun fetchAds(): IOStatus<AdList>
}