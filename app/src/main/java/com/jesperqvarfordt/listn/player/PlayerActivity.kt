package com.jesperqvarfordt.listn.player

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import com.google.android.gms.cast.framework.*
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.base.BaseActivity
import com.jesperqvarfordt.listn.databinding.PlayerActivityBinding
import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.player.di.DaggerPlayerComponent
import com.jesperqvarfordt.listn.player.di.PlayerModule
import kotlinx.android.synthetic.main.player_activity.*
import javax.inject.Inject


class PlayerActivity : BaseActivity(), PlayerContract.View {

    @Inject
    lateinit var presenter: PlayerContract.Presenter
    private lateinit var binding: PlayerActivityBinding

    private lateinit var castContext: CastContext

    private var castSession: CastSession? = null
    private var sessionManager: SessionManager? = null
    private var sessionManagerListener = SessionManagerListenerImpl()

    private inner class SessionManagerListenerImpl : SessionManagerListener<Session> {
        override fun onSessionStarted(p0: Session?, p1: String?) {
            Log.d("asdf", "a")
        }

        override fun onSessionResumeFailed(p0: Session?, p1: Int) {
            Log.d("asdf", "b")
        }

        override fun onSessionSuspended(p0: Session?, p1: Int) {
            Log.d("asdf", "c")
        }

        override fun onSessionEnded(p0: Session?, p1: Int) {
            Log.d("asdf", "d")
        }

        override fun onSessionResumed(p0: Session?, p1: Boolean) {
            Log.d("asdf", "e")
        }

        override fun onSessionStarting(p0: Session?) {
            Log.d("asdf", "f")
        }

        override fun onSessionResuming(p0: Session?, p1: String?) {
            Log.d("asdf", "g")
        }

        override fun onSessionEnding(p0: Session?) {
            Log.d("asdf", "h")
        }

        override fun onSessionStartFailed(p0: Session?, p1: Int) {
            Log.d("asdf", "i")
        }
    }

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

        castContext = CastContext.getSharedInstance(this)
        sessionManager = castContext.sessionManager
        CastButtonFactory.setUpMediaRouteButton(this, chromeCastButton)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.no_move, R.anim.exit_down)
    }

    override fun onResume() {
        super.onResume()
        castSession = sessionManager?.currentCastSession
        sessionManager?.addSessionManagerListener(sessionManagerListener)
        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    private fun setUpListeners() {
        playPause.setOnClickListener({
            presenter.playPauseClicked()
        })

        next.setOnClickListener({
            presenter.skipForwardClicked()
        })

        previous.setOnClickListener({
            presenter.skipBackwardsClicked()
        })
    }

    override fun updateUi(data: CombinedInfo) {
        binding.data = data
        binding.notifyChange()
    }
}
