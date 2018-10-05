package com.example.daggermvp.ui.activities.result

import com.example.daggermvp.ui.BaseContract

class ResultContract {

    interface View: BaseContract.View {

    }

    interface Presenter: BaseContract.Presenter<View> {

    }
}