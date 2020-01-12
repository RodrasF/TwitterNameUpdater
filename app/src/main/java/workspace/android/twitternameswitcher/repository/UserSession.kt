package workspace.android.twitternameswitcher.repository

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "TwitterNameSwitcherSharedPreferences"

private const val PRIVATE_MODE = 0

private const val USER_KEY = "userKey"

private const val USER_SECRET = "userSecrete"

class UserSession(context : Context) {

    // SharedPreferences reference
    private val prefs = context.getSharedPreferences(
        PREFS_NAME,
        PRIVATE_MODE
    )

    // Editor reference for Shared preferences
    private val editor: SharedPreferences.Editor = prefs.edit()

    //Create login session
    fun createUserLoginSession(userKey: String, userSecret: String) {

        editor.putString(USER_KEY, userKey)

        editor.putString(USER_SECRET, userSecret)

        // apply changes
        editor.apply()
    }

    // Check for login
    fun isUserLoggedIn(): Boolean{
        return (userKey != null && userSecret != null)
    }

    val userKey: String?
        get() = prefs.getString(USER_KEY,null)

    val userSecret: String?
        get() = prefs.getString(USER_SECRET,null)

    /**
     * Clear session details
     */
    fun logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear()
        editor.apply()
    }
}
