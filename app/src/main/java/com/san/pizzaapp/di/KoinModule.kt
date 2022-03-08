package com.san.pizzaapp.di

import android.content.Context
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.san.pizzaapp.network.ApiService
import com.san.pizzaapp.repo.Repository
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.utils.Utils
import com.san.pizzaapp.viewModel.MainViewModel
import com.san.pizzaapp.viewModel.RoomDbViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val roomDbViewModel = module {
    viewModel {
        RoomDbViewModel(get())
    }
}

val roomDatabaseModule = module {

    fun roomDB(context: Context): CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java, Utils().DB_NAME
        ).allowMainThreadQueries().build()
    }

    single { get<CartDatabase>().cartDao() }

    single { roomDB(get()) }

}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

val repositoryModule = module {
    single {
        Repository(get())
    }
}

val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    single { provideUseApi(get()) }
}

val retrofitModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Utils().BASEURL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(httpClient)
            .build()
    }

    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}