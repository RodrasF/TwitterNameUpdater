package workspace.android.twitternameswitcher.repository.http

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import workspace.android.twitternameswitcher.App
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


object HttpService{

    val TAG = HttpService::class.java.simpleName

    private lateinit var reqQueue :RequestQueue

    private val future = RequestFuture.newFuture<String>()

    fun init(application : App) {
        reqQueue = Volley.newRequestQueue(application)
    }

    fun post(url: String, headers: Map<String, String> = mapOf() , cb: (String, RequestStatus) -> Unit) {

        val req = object : StringRequest(
            Method.POST,
            url,
            Response.Listener<String>{
                    response ->
                Log.d(TAG, "/POST request OK! Response: $response")
                cb(response, RequestStatus.SUCCESS)
            },
            Response.ErrorListener{
                    error ->
                Log.e(TAG, "Request fail! Error: ${error.message}")
                cb(error.message?:"Unknown error!",RequestStatus.ERROR)
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>  = headers
        }
        reqQueue.add(req)
    }

    fun get(url: String, headers: Map<String, String> = mapOf() , cb: (String, RequestStatus) -> Unit) {

        val req = object : StringRequest(
                Method.GET,
                url,
                Response.Listener<String>{
                    response ->
                    Log.d(TAG, "/GET request OK! Response: $response")
                    cb(response, RequestStatus.SUCCESS)
                },
                Response.ErrorListener{
                    error ->
                    Log.e(TAG, "Request fail! Error: ${error.message}")
                    cb(error.message?:"Unknown error!",RequestStatus.ERROR)
                })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>  = headers
        }
        reqQueue.add(req)
    }



    fun getFuture(url: String, headers: Map<String, String> = mapOf(),cb: (String, RequestStatus) -> Unit){

        val future = RequestFuture.newFuture<String>()

        val request = object : StringRequest(
                Method.GET,
                url,
                future,
                future)
        {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>  = headers
        }

        reqQueue.add(request)

        try {
            val response = future.get(10, TimeUnit.SECONDS)
            Log.i(TAG,"RESPONSE = $response")
            cb(response, RequestStatus.SUCCESS)
        } catch (e: InterruptedException) {
            return cb("", RequestStatus.ERROR)
        } catch (e: ExecutionException) {
            return cb("", RequestStatus.ERROR)
        } catch (e: TimeoutException) {
            return cb("", RequestStatus.ERROR)
        }
    }
}