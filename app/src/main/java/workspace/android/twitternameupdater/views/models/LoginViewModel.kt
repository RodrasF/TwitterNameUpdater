package workspace.android.twitternameupdater.views.models

import androidx.lifecycle.ViewModel
import workspace.android.twitternameupdater.repository.Repository


class LoginViewModel(
    val repo : Repository
) : ViewModel() {

    private val TAG = LoginViewModel::class.java.simpleName


}