package com.hobbajt.ynd.main.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.hobbajt.ynd.R
import com.hobbajt.ynd.main.FragmentsManager
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, MainContractor.View
{
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var fragmentsManager: FragmentsManager

    @Inject
    lateinit var presenter: MainPresenter


    override fun onCreate(savedInstanceState: Bundle?)
    {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        setContentView(R.layout.activity_main)
    }

    override fun start()
    {
        fragmentsManager.onStart()
    }

    override fun onResume()
    {
        super.onResume()
        presenter.onViewReady()
    }

    override fun onBackPressed()
    {
        if (!fragmentsManager.onBackPressed())
        {
            super.onBackPressed()
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        presenter.detachView()
    }


    override fun closeApp()
    {
        finishAffinity()
    }

    // region External Storage Permission
    override fun requestExternalStorageIfRequired()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            when (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                PackageManager.PERMISSION_GRANTED -> presenter.onExternalStoragePermissionGranted()
                PackageManager.PERMISSION_DENIED -> requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (grantResults[0])
        {
            PackageManager.PERMISSION_GRANTED -> presenter.onExternalStoragePermissionGranted()
            PackageManager.PERMISSION_DENIED -> presenter.onExternalStoragePermissionDenied()
        }
    }
    // endregion External Storage Permission

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector
}
