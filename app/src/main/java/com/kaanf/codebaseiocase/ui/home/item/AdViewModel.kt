package com.kaanf.codebaseiocase.ui.home.item

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.kaanf.codebaseiocase.data.model.Ad
import java.io.Serializable

class AdViewModel(private val listener: Listener, val ad: Ad) : ViewModel(), Serializable {
    val adObservable: ObservableField<Ad> = ObservableField()
    val category: ObservableField<String> = ObservableField()
    val price: ObservableField<String> = ObservableField()
    val region: ObservableField<String> = ObservableField()
    val roomCount: ObservableField<String> = ObservableField()
    val bathroomCount: ObservableField<String> = ObservableField()
    val grossArea: ObservableField<String> = ObservableField()
    val netArea: ObservableField<String> = ObservableField()
    val date: ObservableField<String> = ObservableField()
    val badge: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()

    val roomCountInDetail: ObservableField<String> = ObservableField()
    val grossAreaInDetail: ObservableField<String> = ObservableField()
    val netAreaInDetail: ObservableField<String> = ObservableField()

    var images: List<String> = listOf()

    val isBadgeShown: ObservableField<Boolean> = ObservableField(false)

    init {
        adObservable.set(ad)
        category.set(ad.category)
        price.set("${ad.price}₺")
        region.set("${ad.city} / ${ad.district} / ${ad.neighborhood}")
        roomCount.set("${ad.roomCount} Oda")
        bathroomCount.set("${ad.bathCount} Banyo")
        grossArea.set("${ad.gross} brüt m²")
        netArea.set("${ad.net} net m²")
        date.set(ad.createdDate)
        description.set(ad.description)

        grossAreaInDetail.set("${ad.gross} m²")
        netAreaInDetail.set("${ad.net} m²")
        roomCountInDetail.set(ad.room)

        images = ad.images

        ad.label?.let { label ->
            isBadgeShown.set(true)
            badge.set(label)
        }
    }

    fun onAdClicked() {
        listener.onAdClicked(ad = ad)
    }


    interface Listener {
        fun onAdClicked(ad: Ad)
    }
}