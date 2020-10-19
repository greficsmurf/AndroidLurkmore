package com.app.lurkmoarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject



class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private val appBarConfig: AppBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.search_view_title)
        setSupportActionBar(toolbar)

        toolbar.setupWithNavController(navController, appBarConfig)
    }

    fun restoreActionBar(){
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfig)

        supportActionBar?.show()
    }

    fun setUpActionBar(toolbar: Toolbar){
        supportActionBar?.hide()

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfig)
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}