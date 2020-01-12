package workspace.android.twitternameswitcher

import android.app.Application
import android.util.Log
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import workspace.android.twitternameswitcher.repository.Repository


class App : Application() {

    lateinit var repository: Repository
        private set

    lateinit var twitterAuthClient : TwitterAuthClient

    override fun onCreate() {
        super.onCreate()
        repository = Repository(this)

        val config : TwitterConfig = TwitterConfig.Builder(this)
        .logger(DefaultLogger(Log.DEBUG))
        .twitterAuthConfig(TwitterAuthConfig(resources.getString(R.string.twitter_api_key), resources.getString(R.string.twitter_api_secret)))
        .debug(true)
        .build()
        Twitter.initialize(config)
        twitterAuthClient = TwitterAuthClient()
    }
}