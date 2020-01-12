package workspace.android.twitternameswitcher.views.models

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.BasicHttpParams
import org.apache.http.params.HttpProtocolParams
import org.json.JSONObject
import workspace.android.twitternameswitcher.repository.Repository


class MainViewModel(
    val repo : Repository
) : ViewModel() {

    private val TAG = "MainViewModel"

    var httpOauthConsumer: CommonsHttpOAuthConsumer? = null

    private val currentName = MutableLiveData<String>()

    init {
        postName(null)
    }

    fun liveCurrentName() : MutableLiveData<String>{
        return currentName
    }

    fun initConsumer(api_key: String, api_secret: String) {
        val task = GetConsumerTask()
        task.execute(api_key, api_secret)
    }

    inner class GetConsumerTask : AsyncTask<String?, Void, Void?>() {

        override fun doInBackground(vararg params: String?) : Void? {
            val apiKey = params[0]
            val apiSecret = params[1]
            httpOauthConsumer = CommonsHttpOAuthConsumer(apiKey, apiSecret)
            return null
        }
    }

    fun postName(name : String?) {
        if(httpOauthConsumer == null){
            Log.d(TAG, "Consumer equals null at post name")
        }else {
            val postNameTask = PostNameTask()
            postNameTask.execute(name)
        }
    }

    inner class PostNameTask : AsyncTask<String?, Void, JSONObject>() {

        override fun doInBackground(vararg params: String?) : JSONObject {
            val name = params[0]
            val httpClient = DefaultHttpClient()

            val post = HttpPost("https://api.twitter.com/1.1/account/update_profile.json")
            val parameters = BasicHttpParams()
            if(!name.isNullOrBlank()) {
                parameters.setParameter("name", name)
            }
            HttpProtocolParams.setUseExpectContinue(parameters, false)
            post.params = parameters
            // sign the request to authenticate
            httpOauthConsumer?.sign(post)
            val responsex = httpClient.execute(post, BasicResponseHandler())

            return JSONObject(responsex)
        }

        override fun onPostExecute(result: JSONObject) {
            currentName.postValue(result.getString("name"))
        }
    }
}