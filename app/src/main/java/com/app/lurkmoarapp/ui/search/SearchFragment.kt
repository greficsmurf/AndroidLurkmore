package com.app.lurkmoarapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.view.marginRight
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.lurkmoarapp.MainActivity
import com.app.lurkmoarapp.R
import com.app.lurkmoarapp.apimodels.ApiSearchItem
import com.app.lurkmoarapp.base.BaseFragment
import com.app.lurkmoarapp.databinding.FragmentSearchBinding
import com.app.lurkmoarapp.databinding.SearchResultItemBinding
import com.app.lurkmoarapp.di.Injectable
import com.app.lurkmoarapp.domain.SearchItem
import com.app.lurkmoarapp.domain.mapper.asApiModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask


class SearchFragment : BaseFragment(){

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    lateinit var vm: SearchViewModel

    private lateinit var searchView: EditText

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate<FragmentSearchBinding>(
            inflater, R.layout.fragment_search, container,false
        )

        vm = vmFactory.create(SearchViewModel::class.java)

        val searchResultAdapter = SearchResultsAdapter(vm)

        binding.apply {
            searchResult.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = searchResultAdapter
            }

            viewModel = vm
            lifecycleOwner = viewLifecycleOwner
        }

        vm.searchString.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(::searchView.isInitialized)
                    if(it != searchView.text.toString())
                    {
                        searchView.setText(it)
                        searchView.setSelection(it.length)
                    }
            }
        })

        binding.historyDeleteButton.setOnClickListener {
            vm.deleteAllHistory()
        }
        vm.searchResultResource.observe(viewLifecycleOwner, Observer {
            it.data?.let {searchRes->
                searchResultAdapter.submitList(searchRes.searchResults)
            }
        })

        return binding.root
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment_menu, menu)

        try {
            val menuItem = menu.findItem(R.id.searchBar)
            searchView = menuItem.actionView as EditText

            searchView.apply {
                addTextChangedListener(
                    object : TextWatcher{
                        private var timer = Timer()
                        private val DELAY = 700L

                        override fun afterTextChanged(s: Editable?) {
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            timer.cancel()

                            if(s.isNullOrBlank())
                            {
                                vm.setSearchString(s.toString())
                            }

                            timer = Timer()
                            timer.schedule(
                                timerTask {
                                    vm.setSearchString(s.toString())
                                },
                                DELAY
                            )
                        }

                    }
                )
                width = Int.MAX_VALUE
                setText(vm.searchString.value)
                selectAll()

                requestFocus()
                toggleKeyBoard()
            }
        }catch (e: Exception){
            Snackbar.make(requireView(), getString(R.string.search_bar_init_error), Snackbar.LENGTH_LONG).show()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStop() {
        Timber.d("SearchFragment OnStop")

        super.onStop()
        toggleKeyBoard()
    }

    override fun onDestroyView() {
        Timber.d("SearchFragment OnDestroyView")

        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.d("SearchFragment OnDestroy")

        super.onDestroy()
    }

    private class SearchResultsAdapter(
        private val vm: SearchViewModel
    ) : ListAdapter<SearchItem, RecyclerView.ViewHolder>(diffUtil){

        companion object{
            val diffUtil = object : DiffUtil.ItemCallback<SearchItem>(){
                override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem) = oldItem == newItem

                override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem) = oldItem == newItem
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding: SearchResultItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.search_result_item,
                parent, false
            )

            return SearchResultsViewHolder(binding, vm)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is SearchResultsViewHolder)
                holder.bind(getItem(position))
        }

    }

    private class SearchResultsViewHolder(
        private val binding: SearchResultItemBinding,
        private val vm: SearchViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchItem: SearchItem) {
            binding.mainLayout.setOnClickListener {
                if(searchItem.isHistory)
                {
                    vm.setSearchString(searchItem.title)
                }
                else{
                    val action = SearchFragmentDirections.actionSearchFragmentToPageFragment(searchItem.title, searchItem.title.toUpperCase(
                        Locale.ROOT
                    )
                    )
                    it.findNavController().navigate(action)
                }

            }
            binding.item = searchItem
            binding.executePendingBindings()
        }

    }
}