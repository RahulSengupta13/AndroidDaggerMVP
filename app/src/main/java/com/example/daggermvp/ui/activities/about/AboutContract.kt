package com.example.daggermvp.ui.activities.about

import com.example.daggermvp.ui.BaseContract

class AboutContract {

    interface View: BaseContract.View

    interface Presenter: BaseContract.Presenter<View>

}