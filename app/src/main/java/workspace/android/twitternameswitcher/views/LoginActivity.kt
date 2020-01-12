package workspace.android.twitternameswitcher.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import workspace.android.twitternameswitcher.App
import workspace.android.twitternameswitcher.R
import workspace.android.twitternameswitcher.views.models.LoginViewModel
import workspace.android.twitternameswitcher.views.models.viewModel
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import kotlinx.android.synthetic.main.activity_login.*




class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.java.simpleName

    private lateinit var model : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        model = this.viewModel()

        login_button.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                // Do something with result, which provides a TwitterSession for making API calls
                val newTwitterSession = result?.data
                Log.d("Twitter", "Success: Getting Twitter Data")
                getTwitterData(newTwitterSession)

            }

            override fun failure(exception: TwitterException) {
                // Do something on failure
                Log.e("Twitter", "Failed to authenticate user " + exception.message)
            }
        }

    }

    private fun getTwitterData(twitterSession: TwitterSession?) {
        val twitterApiClient = TwitterApiClient(twitterSession)
        val getUserCall = twitterApiClient.accountService.verifyCredentials(true, false, false)
        getUserCall.enqueue(object : Callback<User>() {
            override fun success(result: Result<User>?) {
                val user = result?.data
                val name  = user?.name
                val picture = user?.profileImageUrlHttps?.replace("_normal", "")

                Log.e(
                    TAG,
                    "Twitter: $name"
                )

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("name", name)
                intent.putExtra("picture", picture)
                startActivity(intent)
            }

            override fun failure(exception: TwitterException) {
                Log.e("Twitter", "Failed to get user data " + exception.message)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the login button.
        login_button.onActivityResult(requestCode, resultCode, data)
    }

}
