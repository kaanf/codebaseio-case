package com.kaanf.codebaseiocase.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanf.codebaseiocase.R
import com.kaanf.codebaseiocase.data.model.AdList
import com.kaanf.codebaseiocase.databinding.FragmentHomeBinding
import com.kaanf.codebaseiocase.ui.AdViewModel
import com.kaanf.codebaseiocase.ui.AdsAdapter
import com.kaanf.codebaseiocase.ui.MainActivity
import com.kaanf.codebaseiocase.utils.IOStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeViewModel.Navigator {
    private val viewModel by viewModels<HomeViewModel>()

    private var binding: FragmentHomeBinding? = null

    private lateinit var adsAdapter: AdsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this

        lifecycleScope.launch {
            withStarted {
                viewModel.fetch()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel

        subscribe()

        binding?.statusBarSpacer?.layoutParams?.let {
            it.height = (activity as MainActivity).statusBarHeight
        }

        return binding?.root
    }

    private fun subscribe() {
        viewModel.ads.observe(viewLifecycleOwner) { ioStatus ->
            when (ioStatus) {
                is IOStatus.Success -> {
                    init(ioStatus.data)
                }

                is IOStatus.Failure -> {
                    showError()
                }

                is IOStatus.Loading -> {
                    lifecycleScope.launch {
                        delay(500L)

                        viewModel.setVisibilityState(ioStatus.isLoading)

                        if (ioStatus.isLoading)
                            binding?.shimmerPlaceholder?.startShimmer()
                        else
                            binding?.shimmerPlaceholder?.stopShimmer()
                    }
                }
            }
        }
    }

    private fun init(ads: AdList) {
        val layoutManager = LinearLayoutManager(context)

        if (!::adsAdapter.isInitialized)
            adsAdapter = AdsAdapter(viewModel.getViewModels(ads))

        binding?.ads?.apply {
            this@apply.layoutManager = layoutManager
            adapter = adsAdapter

            getDrawable(context, R.drawable.divider)?.let { divider ->
                val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
                itemDecoration.setDrawable(divider)
                addItemDecoration(itemDecoration)
            }
        }

        binding?.search?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
                adsAdapter.filter.filter(query)
            }
        })
    }

    private fun showError() {
        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onAdClicked(adViewModel: AdViewModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(adViewModel)
        findNavController().navigate(action)
    }
}