package com.app.lurkmoarapp.ui.page

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.lurkmoarapp.R
import com.app.lurkmoarapp.base.BaseFragment
import com.app.lurkmoarapp.databinding.FragmentPageBinding
import com.app.lurkmoarapp.databinding.SpannedItemBinding
import com.app.lurkmoarapp.databinding.TableContentItemBinding
import com.app.lurkmoarapp.di.Injectable
import com.app.lurkmoarapp.domain.models.ContentTableItem
import com.app.lurkmoarapp.parser.models.Section
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class PageFragment : BaseFragment() {
    companion object{
        const val TAG = "PageFragment"
    }

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private val vm: PageViewModel by viewModels {
        vmFactory
    }

    private val args: PageFragmentArgs by navArgs()

    private lateinit var binding: FragmentPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_page,
            container,
            false
        )

        val pageSpannedAdapter = PageSpannedAdapter()
        val tableContentsAdapter = TableOfContentsAdapter(vm)
        val pageRecyclerLayoutManager = LinearLayoutManager(context)

        binding.apply {
            viewModel = vm
            lifecycleOwner = viewLifecycleOwner

            recycler.apply {
                adapter = pageSpannedAdapter
                layoutManager = pageRecyclerLayoutManager
            }
            contentListRecycler.apply {
                adapter = tableContentsAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        vm.parsedWikiTextSections.observe(viewLifecycleOwner, Observer {
            pageSpannedAdapter.submitList(it)
            Timber.d("parsedWikiTextSections observed")
        })

        vm.tableOfContentTableItem.observe(viewLifecycleOwner, Observer {
            tableContentsAdapter.submitList(it)
        })

        vm.currentSectionNumber.observe(viewLifecycleOwner, Observer {currentSection ->
            binding.apply {
                tableContentsDrawer.closeDrawer(GravityCompat.END)

                recycler.apply {
                    val index = vm.parsedWikiTextSections.value?.indexOfFirst {section ->
                        section.index == currentSection
                    } ?: 0
                    pageRecyclerLayoutManager.scrollToPositionWithOffset(index, 0)
//                    smoothScrollToPosition(index)
                }
            }
        })
        vm.shortInfoPageName.observe(viewLifecycleOwner, Observer {
            PagePreviewBottomSheetDialog.getInstance().apply {
                show(
                    this@PageFragment.requireActivity().supportFragmentManager,
                    PagePreviewBottomSheetDialog.TAG
                )
            }
        })

        vm.setPageName(args.pageName)

        return binding.root
    }


    private class PageSpannedAdapter : ListAdapter<Section, RecyclerView.ViewHolder>(diffUtil){
        private val HEADER_TYPE = 0
        private val BODY_TYPE = 1

        companion object{
            val diffUtil = object : DiffUtil.ItemCallback<Section>(){
                override fun areItemsTheSame(oldItem: Section, newItem: Section) = oldItem.title == newItem.title

                override fun areContentsTheSame(oldItem: Section, newItem: Section) = oldItem == newItem
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding: SpannedItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.spanned_item,
                parent, false
            )

            return SpannedViewHolder(binding)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is SpannedViewHolder)
                holder.bind(getItem(position))
        }

    }

    private class SpannedViewHolder(
        private val binding: SpannedItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(paragraph: Section) {
            binding.apply {
                isTitleEmpty = title.isNullOrEmpty()
                title = paragraph.title
                text = paragraph.text
                spannedText.movementMethod = LinkMovementMethod.getInstance()

                executePendingBindings()
            }
        }
    }



    private class TableOfContentsAdapter(val vm: PageViewModel) : ListAdapter<ContentTableItem, RecyclerView.ViewHolder>(diffUtil){
        companion object{
            val diffUtil = object : DiffUtil.ItemCallback<ContentTableItem>(){
                override fun areItemsTheSame(
                    oldItem: ContentTableItem,
                    newItem: ContentTableItem
                ): Boolean = oldItem.number == newItem.number

                override fun areContentsTheSame(
                    oldItem: ContentTableItem,
                    newItem: ContentTableItem
                ): Boolean  = oldItem == newItem
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding : TableContentItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.table_content_item,
                parent,
                false
            )

            return TableOfContentsViewHolder(binding, vm)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is TableOfContentsViewHolder)
                holder.bind(getItem(position))

        }
    }

    private class TableOfContentsViewHolder(val binding: TableContentItemBinding,
                                            val vm: PageViewModel) : RecyclerView.ViewHolder(binding.root){

        fun bind(tableItem : ContentTableItem){
            binding.contentTableItem.setOnClickListener {
                vm.setCurrentSection(tableItem.number)
            }
            binding.apply {
                title.text = tableItem.title
            }
            when(tableItem.number.count {
                it == '.'
            }){
                1 -> {
                    binding.title.textSize = 18F
                }
                2 -> {
                    binding.title.textSize = 16F
                }
                3 -> {
                    binding.title.textSize = 14F
                }
            }
        }
    }

}