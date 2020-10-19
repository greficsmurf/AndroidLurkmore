package com.app.lurkmoarapp.base

import android.content.Context
import android.hardware.input.InputManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.lurkmoarapp.di.Injectable
import com.app.lurkmoarapp.viewModel.ViewModelFactory

open class BaseFragment : Fragment(), Injectable {

    protected fun toggleKeyBoard() = (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .toggleSoftInput(0, 0)

    protected fun <T> getViewModel(factory: ViewModelFactory){
        ViewModelProvider(this)
    }
}