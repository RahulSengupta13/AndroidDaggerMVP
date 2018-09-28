package com.example.daggermvp.ui.autovision

import com.example.daggermvp.ui.base.BaseContract

class AutoVisionContract {

    interface View: BaseContract.View {

    }

    interface Presenter: BaseContract.Presenter<View> {

    }
}