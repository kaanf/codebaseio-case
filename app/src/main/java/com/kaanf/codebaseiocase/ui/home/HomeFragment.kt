package com.kaanf.codebaseiocase.ui.home

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.kaanf.codebaseiocase.ui.AdViewModel
import com.kaanf.codebaseiocase.ui.AdsAdapter
import com.kaanf.codebaseiocase.ui.MainActivity
import com.kaanf.codebaseiocase.ui.SimpleDividerItemDecoration
import com.kaanf.codebaseiocase.utils.IOStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeViewModel.Navigator {
    private val viewModel by viewModels<HomeViewModel>()

    private var binding: FragmentHomeBinding? = null

    private lateinit var adsAdapter: AdsAdapter

    private var isFirstInitialized = true

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

        setUI()

        subscribe()

        return binding?.root
    }

    private fun subscribe() {
        viewModel.ads.observe(viewLifecycleOwner) { ioStatus ->
            when (ioStatus) {
                is IOStatus.Success -> {
                    if (isFirstInitialized) {
                        isFirstInitialized = false
                        init(ioStatus.data)
                    }
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
        binding?.search?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.setClearIconVisibility(query.isNotEmpty())
                viewModel.updateSearchQuery(query.toString())

                adsAdapter.filter.filter(query)
            }
        })

        binding?.search?.setOnFocusChangeListener { view, isFocused ->
            context?.let { context ->
                val searchDrawableFocused = getDrawable(context, R.drawable.bg_search_focused)
                val searchDrawableNormal = getDrawable(context, R.drawable.bg_search)

                val duration = 200

                val newBackground = if (isFocused) searchDrawableFocused else searchDrawableNormal
                val currentBackground = binding?.searchRoot?.background

                if (currentBackground is TransitionDrawable)
                    currentBackground.reverseTransition(duration)
                else {
                    val transition = TransitionDrawable(arrayOf(currentBackground, newBackground))

                    binding?.searchRoot?.background = transition
                    transition.startTransition(duration)
                }
            }
        }

        binding?.search?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val inputMethodManager: InputMethodManager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                    binding?.search?.clearFocus()

                    return true
                }

                return false
            }
        })

        binding?.statusBarSpacer?.layoutParams?.let {
            it.height = (activity as MainActivity).statusBarHeight
        }

        binding?.ads?.setPadding(0, 0, 0, (activity as MainActivity).navigationBarHeight)
    }

    private fun init(ads: AdList) {
        val layoutManager = LinearLayoutManager(context)

        adsAdapter = AdsAdapter(viewModel.getViewModels(ads))

        Log.i("App.tag", "init: called.")

        binding?.ads?.apply {
            this@apply.layoutManager = layoutManager
            adapter = adsAdapter

            getDrawable(context, R.drawable.divider)?.let { divider ->
                val itemDecoration = SimpleDividerItemDecoration(this.context, R.drawable.divider)
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