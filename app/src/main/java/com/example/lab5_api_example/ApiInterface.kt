import com.example.lab5_api_example.Joke
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("random_joke")
    fun getRandomJoke(): Call<Joke>

    @GET("jokes/programming/random")
    fun getRandomProgrammingJoke(): Call<Joke>

    @GET("jokes/general/random")
    fun getRandomGeneralJoke(): Call<Joke>

    @GET("jokes/{id}")
    fun getJokeByID(@Path("id") id: Int): Call<Joke>
}
