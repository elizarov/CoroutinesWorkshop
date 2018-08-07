package project

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.module.kotlin.*
import okhttp3.*
import retrofit2.*
import retrofit2.Call
import retrofit2.converter.jackson.*
import retrofit2.http.*
import java.util.*

interface GitHubService {
    @GET("orgs/{org}/repos?per_page=100")
    fun listOrgRepos(
        @Path("org") org: String
    ): Call<List<Repo>>

    @GET("repos/{owner}/{repo}/contributors?per_page=100")
    fun listRepoContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<List<User>>
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Repo(
    val id: Long,
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    val login: String,
    val contributions: Int
)

data class RequestData(
    val username: String,
    val password: String,
    val org: String
)

fun createGitHubService(username: String, password: String): GitHubService {
    val authToken = "Basic " + Base64.getEncoder().encode("$username:$password".toByteArray()).toString(Charsets.UTF_8);
    val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .header("Accept", "application/vnd.github.v3+json")
                .header("Authorization", authToken)
            val request = builder.build()
            chain.proceed(request)
        }.build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .client(httpClient)
        .build()
    return retrofit.create(GitHubService::class.java)
}
