package workspace.android.twitternameupdater.views.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.Callback
import org.json.JSONObject
import retrofit2.Response
import workspace.android.twitternameupdater.repository.Preset
import workspace.android.twitternameupdater.repository.Repository
import workspace.android.twitternameupdater.repository.TwitterApiClientExt




class MainViewModel(
    val repo : Repository
) : ViewModel() {

    private val TAG = "MainViewModel"

    private val currentName = MutableLiveData<String>()

    fun liveCurrentName() : MutableLiveData<String>{
        return currentName
    }

    fun saveName(name : String, preset: Preset){
        when(preset) {
            Preset.LIVE -> repo.saveLiveName(name)
            Preset.OFFLINE -> repo.saveOfflineName(name)
        }
    }

    fun getName(preset : Preset) : String{
        return when(preset) {
            Preset.LIVE -> repo.getLiveName()
            Preset.OFFLINE -> repo.getOfflineName()
        }
    }

    fun logout() {
        repo.logout()
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