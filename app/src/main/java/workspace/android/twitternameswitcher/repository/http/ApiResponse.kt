package workspace.android.twitternameswitcher.repository.http


sealed class ApiResponse<T>

data class ApiSuccessResponse<T>(val response : T?) : ApiResponse<T>()

data class ApiErrorResponse<T>(val nullResponse : T?) : ApiResponse<T>()