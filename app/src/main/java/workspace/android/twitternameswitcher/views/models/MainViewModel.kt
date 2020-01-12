package workspace.android.twitternameswitcher.views.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.Callback
import org.json.JSONObject
import retrofit2.Response
import workspace.android.twitternameswitcher.repository.Repository
import workspace.android.twitternameswitcher.repository.TwitterApiClientExt




class MainViewModel(
    val repo : Repository
) : ViewModel() {

    private val TAG = "MainViewModel"

    private val currentName = MutableLiveData<String>()

    fun liveCurrentName() : MutableLiveData<String>{
        return currentName
    }

    fun postName(name : String) {
        val twitterSession = TwitterCore.getInstance().sessionManager.activeSession
        val twitterApiClient = TwitterApiClientExt(twitterSession)

        val call = twitterApiClient.customService.updateName(name)
        call.enqueue(object : Callback<Response<JSONObject>>() {
            override fun success(result: Result<Response<JSONObject>>) {
                currentName.postValue(name)
                Log.e(TAG, "Name updated with success: $name")
            }

            override fun failure(exception: TwitterException) {
                // Do something on failure
                Log.e(TAG, "Failed to update name: " + exception.message)
            }
        })
    }

}