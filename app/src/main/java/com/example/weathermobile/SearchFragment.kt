package com.example.weathermobile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weathermobile.databinding.SearchFragmentBinding
import com.example.weathermobile.SearchViewModel.Event.NavigateToCurrentConditions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var viewModel: SearchViewModel

    private lateinit var binding: SearchFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Search"
        binding = SearchFragmentBinding.bind(view)

        binding.searchButton.setOnClickListener { viewModel.searchButtonClicked() }

        binding.zipCodeEntry.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.updateSearchText(p0.toString())
            }
        })

        viewModel.onViewCreated()

        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is NavigateToCurrentConditions -> navigateToCurrentConditions(it)
                SearchViewModel.Event.SearchError -> handleSearchError()
                SearchViewModel.Event.ViewCreated -> { /* no-op */ }
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { bindView(it) }
    }

    private fun navigateToCurrentConditions(navigateToCurrentConditions: NavigateToCurrentConditions) {
        val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
            navigateToCurrentConditions.currentConditions
        )
        findNavController().navigate(action)
    }

    private fun bindView(state: SearchViewModel.State) {
        binding.searchButton.isEnabled = state.searchButtonEnabled
    }

    private fun handleSearchError() {
        SearchError()
            .show(childFragmentManager, SearchError.TAG)
    }
}