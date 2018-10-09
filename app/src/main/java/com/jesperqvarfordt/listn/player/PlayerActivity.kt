package com.jesperqvarfordt.listn.player

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.databinding.PlayerActivityBinding
import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.player.di.DaggerPlayerComponent
import com.jesperqvarfordt.listn.player.di.PlayerModule
import kotlinx.android.synthetic.main.player_activity.*
import javax.inject.Inject


class PlayerActivity : AppCompatActivity(), PlayerContract.View {

    @Inject
    lateinit var presenter: PlayerContract.Presenter
    private lateinit var binding: PlayerActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_up, R.anim.no_move)
        binding = DataBindingUtil.setContentView(this, R.layout.player_activity)
        DaggerPlayerComponent.builder()
                .appComponent(App.instance.appComponent)
                .playerModule(PlayerModule())
                .build()
                .inject(this)
        setUpListeners()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.no_move, R.anim.exit_down)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    private fun setUpListeners() {
        playPause.setOnClickListener {
            presenter.playPauseClicked()
        }

        next.setOnClickListener {
            presenter.skipForwardClicked()
        }

        previous.setOnClickListener {
            presenter.skipBackwardsClicked()
        }
    }

    override fun updateUi(data: CombinedInfo) {
        binding.data = data
        binding.notifyChange()
    }
}
