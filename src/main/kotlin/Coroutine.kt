import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import schema.github.RetrieveRepositoriesQuery


suspend fun main() {
    // set your own token
    val token = ""
    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val builder: Request.Builder = original.newBuilder().method(original.method(), original.body())
                builder.header("Authorization", "bearer $token")
                chain.proceed(builder.build())
            }
            .build()

    val apolloClient: ApolloClient = ApolloClient.builder()
            .serverUrl("https://api.github.com/graphql")
            .okHttpClient(okHttpClient)
            .build()

    GlobalScope.launch {
        val response = try {
            apolloClient.query(RetrieveRepositoriesQuery(first = 5)).await()
        } catch (e: ApolloException) {
            return@launch
        }
        val viewer = response.data?.viewer
        if (viewer == null || response.hasErrors()) {
            return@launch
        }
        println("${viewer.repositories.nodes}")
    }
}
