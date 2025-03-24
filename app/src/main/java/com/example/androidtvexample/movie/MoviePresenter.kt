package com.example.androidtvexample.movie

import android.view.ViewGroup
import androidx.leanback.widget.Presenter

open class MoviePresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val cardView = MovieCardView(parent.context)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        // not implement
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
    }

    companion object {
        private val TAG = "MoviePresenter"
    }
}

