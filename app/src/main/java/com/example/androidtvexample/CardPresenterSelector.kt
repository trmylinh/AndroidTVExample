package com.example.androidtvexample

import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import com.example.androidtvexample.game_show.GameShow
import com.example.androidtvexample.game_show.GameShowPresenter
import com.example.androidtvexample.movie.Movie
import com.example.androidtvexample.movie.MoviePresenter

/*PresenterSelector hỗ trợ việc hiển thị multiple item trong row*/

class CardPresenterSelector : PresenterSelector() {

    private val moviePresenter by lazy { MoviePresenter() }
    private val gameShowPresenter by lazy { GameShowPresenter() }

    override fun getPresenter(item: Any): Presenter {
        return when (item) {
            is Movie -> {
                moviePresenter
            }
            is GameShow -> {
                gameShowPresenter
            }
            else -> gameShowPresenter
        }
    }

    override fun getPresenters(): Array<Presenter> {
        return arrayOf(moviePresenter, gameShowPresenter)
    }
}

