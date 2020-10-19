package com.app.lurkmoarapp.liveevent

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

open class LiveEvent<T> : MutableLiveData<T>() {

    private val observers = CopyOnWriteArrayList<ObserverWrapper<in T>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    override fun setValue(value: T) {
        observers.forEach {
            it.newValue()
        }
        super.setValue(value)
    }

    override fun removeObserver(observer: Observer<in T>) {
        observers.remove(observer)
        super.removeObserver(observer)
    }

    // Probably buggy
    override fun removeObservers(owner: LifecycleOwner) {
        observers.clear()
        super.removeObservers(owner)
    }

    private class ObserverWrapper<T>(
        private val observer: Observer<in T>
    ) : Observer<T>{
        private val isPending = AtomicBoolean()

        override fun onChanged(t: T) {
            if(isPending.compareAndSet(true, false)){
                observer.onChanged(t)
            }
        }

        fun newValue(){
            isPending.set(true)
        }
    }
}

