import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import schema.github.CreateIssueMutation
import schema.github.RetrieveRepositoriesQuery
import schema.github.type.CreateIssueInput


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

    val repositories = apolloClient.query(RetrieveRepositoriesQuery(first = 30)).await()
    val repoId = repositories.data!!.viewer.repositories.nodes?.first()!!.id
    val name = repositories.data!!.viewer.repositories.nodes?.first()!!.name
    print(name);
    val mutation = CreateIssueMutation(createIssueInput = CreateIssueInput(repositoryId = repoId, title = "New Issue"))
    val result = apolloClient.mutate(mutation).await()
    print(result.errors)
}
