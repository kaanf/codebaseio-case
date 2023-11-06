package com.kaanf.codebaseiocase.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aemerse.slider.model.CarouselItem
import com.kaanf.codebaseiocase.databinding.ItemListBinding
import com.kaanf.codebaseiocase.ui.home.item.AdViewModel
import java.util.Locale
import javax.inject.Inject

class AdsAdapter @Inject constructor() : RecyclerView.Adapter<AdsAdapter.AdsViewHolder>(), Filterable {
    private var adSearchResults: MutableList<AdViewModel> = mutableListOf()
    private var adViewModels: List<AdViewModel> = listOf()

    fun add(adViewModels: List<AdViewModel>) {
        this@AdsAdapter.adViewModels = adViewModels
        adSearchResults = adViewModels.toMutableList()
    }

    inner class AdsViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val adViewModel = adSearchResults[position]

            binding.apply {
                this.adViewModel = adViewModel

                val list = mutableListOf<CarouselItem>()

                adViewModel.images.forEach {
                    list.add(CarouselItem(imageUrl = it))
                }

                carousel.setData(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsViewHolder(binding)
    }

    override fun getItemCount(): Int = adSearchResults.size

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchQuery = constraint.toString()

                val searchedQueryAds = if (searchQuery.isEmpty())
                    adViewModels.toList()
                else
                    adViewModels.filter {
                        it.ad.category.lowercase(Locale.ROOT).contains(searchQuery.lowercase(Locale.ROOT))
                    }

                val searchResults = FilterResults()
                searchResults.values = searchedQueryAds

                return searchResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    adSearchResults = (results?.values as List<AdViewModel>).toMutableList()
                    notifyDataSetChanged()
            }
        }
    }
}
