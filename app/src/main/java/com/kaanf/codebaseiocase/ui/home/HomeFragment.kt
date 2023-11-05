package com.kaanf.codebaseiocase.ui.home

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanf.codebaseiocase.R
import com.kaanf.codebaseiocase.data.model.AdList
import com.kaanf.codebaseiocase.databinding.FragmentHomeBinding
import com.kaanf.codebaseiocase.ui.home.item.AdViewModel
import com.kaanf.codebaseiocase.ui.MainActivity
import com.kaanf.codebaseiocase.utils.IOStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeViewModel.Navigator {
    private val viewModel by viewModels<HomeViewModel>()

    private var binding: FragmentHomeBinding? = null

    @Inject
    lateinit var adsAdapter: AdsAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this

        context?.let {
            linearLayoutManager = LinearLayoutManager(context)
        }

        lifecycleScope.launch {
            withStarted {
                viewModel.fetch()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel

        setUI()

        subscribe()

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
                        delay(1000L)

                        viewModel.setPlaceholderVisibility(ioStatus.isLoading)

                        if (ioStatus.isLoading)
                            binding?.shimmerPlaceholder?.startShimmer()
                        else
                            binding?.shimmerPlaceholder?.stopShimmer()
                    }
                }
            }
        }
    }

    private fun setUI() {
        binding?.apply {
            search.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
                    this@HomeFragment.viewModel.setClearIconVisibility(query.isNotEmpty())
                    this@HomeFragment.viewModel.updateSearchQuery(query.toString())

                    adsAdapter.filter.filter(query)
                }
            })

            search.setOnFocusChangeListener { _, isFocused ->
                context?.let { context ->
                    val searchDrawableFocused = getDrawable(context, R.drawable.bg_search_focused)
                    val searchDrawableNormal = getDrawable(context, R.drawable.bg_search)

                    val duration = 200

                    val newBackground = if (isFocused) searchDrawableFocused else searchDrawableNormal
                    val currentBackground = searchRoot.background

                    if (currentBackground is TransitionDrawable)
                        currentBackground.reverseTransition(duration)
                    else {
                        val transition = TransitionDrawable(arrayOf(currentBackground, newBackground))

                        searchRoot.background = transition
                        transition.startTransition(duration)
                    }
                }
            }

            search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(view: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val inputMethodManager: InputMethodManager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                        search.clearFocus()

                        return true
                    }

                    return false
                }
            })

            statusBarSpacer.layoutParams?.let {
                it.height = (activity as MainActivity).statusBarHeight
            }

            ads.setPadding(0, 0, 0, (activity as MainActivity).navigationBarHeight)

            if (ads.layoutManager == null)
                ads.layoutManager = linearLayoutManager
        }
    }

    private fun init(ads: AdList) {
        adsAdapter.add(
            viewModel.adViewModels.ifEmpty {
                viewModel.getViewModels(ads)
            }
        )

        binding?.ads?.apply {
            adapter = adsAdapter

            getDrawable(context, R.drawable.divider)?.let { divider ->
                val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
                itemDecoration.setDrawable(divider)
                addItemDecoration(itemDecoration)
            }
        }
    }

    private fun showError() {
        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onAdClicked(adViewModel: AdViewModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(adViewModel)
        findNavController().navigate(action)
    }
}