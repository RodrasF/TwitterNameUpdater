package workspace.android.twitternameswitcher.repository

import android.content.Context
import android.util.Log
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import workspace.android.twitternameswitcher.App


class Repository(val context: Context) {

    private val TAG = Repository::class.java.simpleName

    private val session = UserSession(context)

    fun postName() {

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