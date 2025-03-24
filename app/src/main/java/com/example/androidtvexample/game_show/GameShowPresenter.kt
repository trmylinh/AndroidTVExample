package com.example.androidtvexample.game_show

import android.view.ViewGroup
import androidx.leanback.widget.Presenter

open class GameShowPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val cardView = GameShowCardView(parent.context)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        // not implement
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
    }

    companion object {
        private val TAG = "GameShowPresenter"
    }
}

