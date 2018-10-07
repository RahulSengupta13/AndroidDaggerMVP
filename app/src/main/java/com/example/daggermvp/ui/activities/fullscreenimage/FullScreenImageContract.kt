package com.example.daggermvp.ui.activities.fullscreenimage

import com.example.daggermvp.ui.BaseContract

class FullScreenImageContract {

    interface View: BaseContract.View

    interface Presenter: BaseContract.Presenter<View>

}