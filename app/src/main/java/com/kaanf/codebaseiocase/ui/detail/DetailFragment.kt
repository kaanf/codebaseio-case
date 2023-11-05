package com.kaanf.codebaseiocase.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.aemerse.slider.listener.CarouselOnScrollListener
import com.aemerse.slider.model.CarouselItem
import com.kaanf.codebaseiocase.R
import com.kaanf.codebaseiocase.databinding.FragmentDetailBinding
import com.kaanf.codebaseiocase.databinding.FragmentHomeBinding
import com.kaanf.codebaseiocase.ui.AdsAdapter
import com.kaanf.codebaseiocase.ui.MainActivity
import com.kaanf.codebaseiocase.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(), DetailViewModel.Navigator {
    private val viewModel by viewModels<DetailViewModel>()

    private var binding: FragmentDetailBinding? = null

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel

        args.adViewModel?.let { adViewModel ->
            binding?.adViewModel = adViewModel
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.statusBarSpacer?.layoutParams?.let {
            it.height = (activity as MainActivity).statusBarHeight
        }

        setCarousel()
    }

    private fun setCarousel() {
        args.adViewModel?.let { adViewModel ->
            binding.apply {
                this?.carousel?.registerLifecycle(viewLifecycleOwner.lifecycle)

                val list = mutableListOf<CarouselItem>()

                adViewModel.images.forEach {
                    list.add(CarouselItem(imageUrl = it))
                }

                this?.carousel?.setData(list)

                var currentPosition = 0
                var currentCarouselItem = CarouselItem()

                this?.carousel?.onScrollListener = object : CarouselOnScrollListener {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int, position: Int, carouselItem: CarouselItem?) {
                        super.onScrollStateChanged(recyclerView, newState, position, carouselItem)
                        if (newState == 0)
                            if (currentCarouselItem != carouselItem)
                                adViewModel.onImageScrolled(if (currentPosition < position || currentPosition == 0) 1 else -1)

                        currentPosition = position
                        currentCarouselItem = carouselItem ?: CarouselItem()
                    }
                }

            }
        }
    }

    override fun onBackArrowClicked() {
        findNavController().popBackStack()
    }
}