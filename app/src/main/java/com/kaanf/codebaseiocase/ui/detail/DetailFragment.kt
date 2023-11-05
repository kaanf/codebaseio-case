package com.kaanf.codebaseiocase.ui.detail

import android.os.Bundle
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
import com.kaanf.codebaseiocase.databinding.FragmentDetailBinding
import com.kaanf.codebaseiocase.ui.MainActivity
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

            adViewModel.resetImagePosition()
        }

        binding?.statusBarSpacer?.layoutParams?.let {
            it.height = (activity as MainActivity).statusBarHeight
        }

        setCarousel()

        return binding?.root
    }

    private fun setCarousel() {
        args.adViewModel?.let { adViewModel ->
            binding?.apply {
                carousel.registerLifecycle(viewLifecycleOwner.lifecycle)

                val carouselItems = mutableListOf<CarouselItem>()

                adViewModel.images.forEach {
                    carouselItems.add(CarouselItem(imageUrl = it))
                }

                carousel.setData(carouselItems)

                var currentPosition = 0
                var currentCarouselItem = CarouselItem()

                carousel.onScrollListener = object : CarouselOnScrollListener {
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