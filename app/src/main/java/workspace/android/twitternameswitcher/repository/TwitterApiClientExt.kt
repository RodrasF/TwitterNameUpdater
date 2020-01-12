package workspace.android.twitternameswitcher.repository

import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterSession
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

class TwitterApiClientExt(session: TwitterSession) : TwitterApiClient(session) {

    /**
     * Provide CustomService with defined endpoints
     */
    val customService: CustomService
        get() = getService(CustomService::class.java)
}

interface CustomService {

    @FormUrlEncoded
    @POST("/1.1/account/update_profile.json")
    fun updateName( @Field("name") name : String) : Call<Response<JSONObject>>
}