package workspace.android.twitternameswitcher.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import oauth.signpost.basic.DefaultOAuthProvider
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.json.JSONObject
import workspace.android.twitternameswitcher.R
import workspace.android.twitternameswitcher.repository.http.HttpService
import workspace.android.twitternameswitcher.repository.http.RequestStatus


class Repository(val context: Context) {

    private val TAG = Repository::class.java.simpleName

    private val session = UserSession(context)

    val httpOauthConsumer = CommonsHttpOAuthConsumer(
        context.resources.getString(R.string.twitter_api_key), context.resources.getString(
            R.string.twitter_api_secret
        )
    )
    val httpOauthProvider = DefaultOAuthProvider(
        "https://twitter.com/oauth/request_token",
        "https://twitter.com/oauth/access_token",
        "https://twitter.com/oauth/authorize"
    )

    fun getAuthentication(cb: (String?)-> Unit){
        RequestTokenTask{
                response, status ->
            if(status == RequestStatus.SUCCESS) {
                cb(response)
            }else {
                throw IllegalAccessException(response)
            }
        }.execute()
    }

    inner class RequestTokenTask(val cb: (String?, RequestStatus)-> Unit) : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void): Void? {
            try {
                val authUrl = (httpOauthProvider).retrieveRequestToken(
                    httpOauthConsumer, context.resources.getString(R.string.callback_url)
                )
                cb(authUrl, RequestStatus.SUCCESS)
            } catch (e: Exception) {
                cb(e.message,RequestStatus.ERROR)
            }
            return null
        }
    }

    companion object {
        val headers = mapOf<String, String>().toMutableMap()
    }

    fun postName(str : String, cb: (JSONObject?)->Unit){

        PostAsyncTask{
                response, _ ->
            val user = JSONObject(response)
            Log.i(TAG,"User Name = ${user.getString("name")}")
        }.execute("url")
    }



    class GetFutureAsyncTask(val cb: (String,RequestStatus)-> Unit): AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void?{
            HttpService.getFuture(params[0], headers){
                    response,status->
                cb(response,status)
            }
            return null
        }
    }

    class GetAsyncTask(val cb: (String,RequestStatus)-> Unit): AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void?{
            HttpService.get(params[0], headers){
                    response,status->
                cb(response,status)
            }
            return null
        }
    }

    class PostAsyncTask(val cb: (String,RequestStatus)-> Unit): AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void?{
            HttpService.post(params[0], headers){
                    response,status->
                cb(response,status)
            }
            return null
        }
    }

    fun createUserSession(userKey : String, userSecret : String){
        session.createUserLoginSession(userKey, userSecret)
    }

    fun isLoggedIn(): Boolean {
        return session.isUserLoggedIn()
    }

    fun logout() {
        session.logoutUser()
    }

    fun getUserKey(): String? {
        return session.userKey
    }

    fun getUserSecret(): String? {
        return session.userSecret

    }
}