package com.app.lurkmoarapp.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.app.lurkmoarapp.vo.ResourceStatus

@BindingAdapter("android:isVisible")
fun setVisibility(view: View, isVisible: Boolean){
    if(isVisible)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}

@BindingAdapter("android:isVisible")
fun setVisibility(view: View, resourceStatus: ResourceStatus?){
    when(resourceStatus){
        ResourceStatus.SUCCESS -> setVisibility(view, false)
        ResourceStatus.FAILED -> setVisibility(view, false)
        ResourceStatus.LOADING -> setVisibility(view, true)
        null -> setVisibility(view, true)
    }
}

@BindingAdapter("android:isErrorVisible")
fun setErrorVisibility(view: View, resourceStatus: ResourceStatus?){
    when(resourceStatus){
        ResourceStatus.FAILED -> setVisibility(view, true)
        else -> setVisibility(view, false)
    }
}

