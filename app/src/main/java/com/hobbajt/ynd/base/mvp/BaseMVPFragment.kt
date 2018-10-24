package com.hobbajt.ynd.base.mvp

import android.content.Context
import android.os.Bundle
import android.view.View
import com.hobbajt.ynd.main.FragmentsManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseMVPFragment<T : BasePresenter<*>> : DaggerFragment()
{
    @Inject
    lateinit var fragmentsManager: FragmentsManager


    protected fun goBack()
    {
        fragmentsManager.goBack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        loadState(savedInstanceState, arguments)
    }

    abstract fun loadState(configurationChangeState: Bundle?, passedArguments: Bundle?)

    // region Lifecycle
    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        attachView()
    }

    abstract fun attachView()

    abstract fun providePresenter(): T

    override fun onDetach()
    {
        super.onDetach()
        providePresenter().detachView()
    }
    // endregion Lifecycle
}