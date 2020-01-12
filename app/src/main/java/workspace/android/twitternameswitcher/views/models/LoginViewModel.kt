package workspace.android.twitternameswitcher.views.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import workspace.android.twitternameswitcher.R
import workspace.android.twitternameswitcher.repository.Repository
import workspace.android.twitternameswitcher.repository.http.RequestStatus


class LoginViewModel(
    val repo : Repository
) : ViewModel() {

    private val TAG = LoginViewModel::class.java.simpleName

    private val loggedIn = MutableLiveData<Boolean>()

    init{
        loggedIn.value = repo.isLoggedIn()
    }

    fun isLoggedIn(): MutableLiveData<Boolean>{
        return loggedIn
    }

    fun getAuthentication(cb: (String?)-> Unit) {
        repo.getAuthentication(cb)
    }
}