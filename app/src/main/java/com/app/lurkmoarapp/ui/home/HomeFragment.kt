package com.app.lurkmoarapp.ui.home

import android.os.Bundle
import android.os.SystemClock.sleep
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.lurkmoarapp.MainActivity
import com.app.lurkmoarapp.R
import com.app.lurkmoarapp.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.scroll_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var c: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        c = container
        binding.searchButton.setOnClickListener {
            startSearch()
        }

        return binding.root
    }


    private fun startSearch(){
//        val extras = FragmentNavigatorExtras(binding.searchButton to "shared_element_container")

        findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
    }
}