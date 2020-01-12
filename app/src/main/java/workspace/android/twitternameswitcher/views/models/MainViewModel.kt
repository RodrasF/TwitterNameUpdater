package workspace.android.twitternameswitcher.views.models

import android.os.AsyncTask
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

    private val TAG = MainViewModel::class.java.simpleName

    lateinit var httpOauthConsumer: CommonsHttpOAuthConsumer

    private val currentName = MutableLiveData<String>()

    lateinit var currentUser : JSONObject

    init {
        postName(null)
    }

    fun liveCurrentName() : MutableLiveData<String>{
        return currentName
    }

    fun postName(name : String?) {
        val postNameTask = PostNameTask()
        postNameTask.execute(name)
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
            httpOauthConsumer.sign(post)
            val responsex = httpClient.execute(post, BasicResponseHandler())

            return JSONObject(responsex)
        }

        override fun onPostExecute(result: JSONObject) {
            currentName.value = result.getString("name")
            currentUser = result
        }
    }
}