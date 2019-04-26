package project

import retrofit2.*

fun errorResponse(response: Response<*>): Nothing =
    throw ErrorResponse(response)

class ErrorResponse(response: Response<*>) : Exception(
    "Failed with ${response.code()}: ${response.message()}\n${response.errorBody()?.string()}"
)
