package workspace.android.twitternameupdater.repository

import android.content.Context


class Repository(val context: Context) {

    private val TAG = Repository::class.java.simpleName

    private val session = UserSession(context)

    fun createUserSession(userId : String){
        session.createUserSession(userId)
    }

    fun getUserId(): String? {
        return session.userId
    }

    fun logout() {
        session.logoutUser()
    }

    fun getLiveName(): String {
        return session.liveName!!
    }

    fun getOfflineName(): String {
        return session.offlineName!!

    }

    fun saveLiveName(name : String) {
        if(name.isEmpty()){
            session.removeLiveName()
        }else{
            session.setLiveName(name)
        }
    }

    fun saveOfflineName(name : String) {
        if(name.isEmpty()){
            session.removeOfflineName()
        }else{
            session.setOfflineName(name)
        }
    }
}