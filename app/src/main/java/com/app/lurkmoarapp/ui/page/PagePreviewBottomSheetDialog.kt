package com.app.lurkmoarapp.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.lurkmoarapp.R
import com.app.lurkmoarapp.databinding.PagePreviewBottomSheetDialogBinding
import com.app.lurkmoarapp.di.Injectable
import com.app.lurkmoarapp.viewModel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*
import javax.inject.Inject

class PagePreviewBottomSheetDialog @Inject constructor() : BottomSheetDialogFragment(), Injectable {

    @Inject
    lateinit var vmFactory: ViewModelFactory

    private val vm: PageViewModel by viewModels {
        vmFactory
    }

    companion object {
        const val TAG = "PagePreviewBottomSheetDialog"

        fun getInstance() =
            PagePreviewBottomSheetDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<PagePreviewBottomSheetDialogBinding>(
            inflater,
            R.layout.page_preview_bottom_sheet_dialog,
            container,false
        )

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            pageHeaderText = vm.clickedPagePreviewText
            pageTitleText = vm.clickedPagePreviewTitle
        }

        vm.clickedPagePreviewResource.observe(viewLifecycleOwner, Observer {
            binding.resource = it
        })

        binding.openPageBtn.setOnClickListener {
            vm.shortInfoPageName.value?.let{
                val action = PageFragmentDirections.actionPageFragmentSelf(it, it.toUpperCase(Locale.ROOT))
                findNavController().navigate(action)

                dismiss()
            }
        }
        return binding.root
    }
}