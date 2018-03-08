package com.jesperqvarfordt.listn.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.home.di.DaggerHomeComponent
import com.jesperqvarfordt.listn.home.di.HomeModule
import com.jesperqvarfordt.listn.tracks.ExploreFragment
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    private val exploreFragment = ExploreFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        DaggerHomeComponent.builder()
                .appComponent(App.instance.appComponent)
                .homeModule(HomeModule())
                .build()
                .inject(this)

        supportFragmentManager.beginTransaction()
                .replace(contentView.id, exploreFragment)
                .commit()

    }

    override fun onBackPressed() {
        if (exploreFragment.canBack()) {
            exploreFragment.backButtonPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.viewDestroyed()
    }

}