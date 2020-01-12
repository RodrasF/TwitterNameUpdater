package workspace.android.twitternameswitcher.views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.ActionMode
import androidx.lifecycle.Observer
import oauth.signpost.OAuthProvider
import oauth.signpost.basic.DefaultOAuthProvider
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import workspace.android.twitternameswitcher.views.models.LoginViewModel
import workspace.android.twitternameswitcher.R
import workspace.android.twitternameswitcher.views.models.viewModel

class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.java.simpleName

    private lateinit var model : LoginViewModel

    lateinit var httpOauthConsumer: CommonsHttpOAuthConsumer
    lateinit var httpOauthprovider: OAuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = this.viewModel()

        model.isLoggedIn().observe(this, Observer<Boolean> {
            if(it){
                Log.i(TAG, "Logged in!")
                startMain()
            }else{
                model.getAuthentication{
                        authUrl ->
                    startBrowserLogin(authUrl)
                }
            }
        })

    }

    private fun startMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    companion object {
        const val BROWSER_LOGIN_REQUEST = 1  // The request code
    }

    private fun startBrowserLogin(authUrl : String?) {
        startActivityForResult( Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)), BROWSER_LOGIN_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == BROWSER_LOGIN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // Get the URI that points to the selected contact
                data.data
            }
        }
    }
}
