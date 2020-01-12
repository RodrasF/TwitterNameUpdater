package workspace.android.twitternameupdater.repository

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "TwitterNameUpdaterSharedPreferences"

private const val PRIVATE_MODE = 0

private const val USER_ID = "userId"
private const val LIVE_NAME = "liveName"
private const val OFFLINE_NAME = "offlineName"


class UserSession(context : Context) {

    // SharedPreferences reference
    private val prefs = context.getSharedPreferences(
        PREFS_NAME,
        PRIVATE_MODE
    )

    // Editor reference for Shared preferences
    private val editor: SharedPreferences.Editor = prefs.edit()

    //Create login session
    fun createUserSession(userId: String) {

        editor.putString(USER_ID, userId)

        // apply changes
        editor.apply()
    }

    fun setLiveName(liveName : String){
        editor.putString(LIVE_NAME, liveName)

        // apply changes
        editor.apply()
    }

    fun setOfflineName(offlineName : String){
        editor.putString(OFFLINE_NAME, offlineName)

        // apply changes
        editor.apply()
    }

    fun removeLiveName(){
        editor.remove(LIVE_NAME)

        editor.apply()
    }

    fun removeOfflineName(){
        editor.remove(OFFLINE_NAME)

        editor.apply()
    }

    val userId: String?
        get() = prefs.getString(USER_ID,null)

    val liveName: String?
        get() = prefs.getString(LIVE_NAME,"")

    val offlineName: String?
        get() = prefs.getString(OFFLINE_NAME,"")


    /**
     * Clear session details
     */
    fun logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear()
        editor.apply()
    }
}
