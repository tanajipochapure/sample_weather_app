package com.mphasis.tanaji.weatherapp.base

import com.mphasis.tanaji.weatherapp.api.APiService
import com.mphasis.tanaji.weatherapp.api.BaseUrl
import com.mphasis.tanaji.weatherapp.api.RetrofitClient.getClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
class CommonModule {

    @Provides
    @Singleton
    fun getRetrofitClient(url: String): Retrofit {
        return getClient(url)
    }

    @Provides
    @Named("createClient")
    fun createClient(): Retrofit = getRetrofitClient(BaseUrl.baseUrlNew)

    @Provides
    @Named("getBaseUrl")
    fun getBaseUrl(@Named("createClient") retrofit: Retrofit):
            APiService = retrofit.create(APiService::class.java)
}