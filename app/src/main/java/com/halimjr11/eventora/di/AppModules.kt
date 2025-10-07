package com.halimjr11.eventora.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.halimjr11.eventora.BuildConfig
import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.core.coroutines.impl.DefaultDispatcherProvider
import com.halimjr11.eventora.data.mapper.EventDataMapper
import com.halimjr11.eventora.data.mapper.impl.EventDataMapperImpl
import com.halimjr11.eventora.data.repository.EventRepository
import com.halimjr11.eventora.data.repository.impl.EventRepositoryImpl
import com.halimjr11.eventora.data.service.EventService
import com.halimjr11.eventora.ui.helper.ThemeManager
import com.halimjr11.eventora.view.features.detail.DetailViewModel
import com.halimjr11.eventora.view.features.home.HomeViewModel
import com.halimjr11.eventora.view.features.search.SearchViewModel
import com.halimjr11.eventora.view.features.settings.SettingViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object AppModules {
    private val coroutineModule = module {
        single<CoroutineDispatcherProvider> {
            DefaultDispatcherProvider()
        }
    }
    private val repositoryModule = module {
        single<EventRepository> { EventRepositoryImpl(get(), get(), get()) }
    }
    private val mapperModule = module {
        single<EventDataMapper> { EventDataMapperImpl(get()) }
    }
    private val loggingModule = module {
        single {
            HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }
    private val chuckerModule = module {
        single {
            ChuckerInterceptor.Builder(androidContext())
                .collector(
                    ChuckerCollector(
                        context = androidContext(),
                        showNotification = true,
                        retentionPeriod = RetentionManager.Period.ONE_HOUR
                    )
                )
                .maxContentLength(250_000L)
                .alwaysReadResponseBody(true)
                .build()
        }
    }

    private val serviceModule = module {

        single {
            OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .addInterceptor(get<ChuckerInterceptor>())
                .build()
        }

        single {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get<OkHttpClient>())
                .build()
        }

        single<EventService> { get<Retrofit>().create(EventService::class.java) }
    }

    private val featureModule = module {
        viewModel { HomeViewModel(get(), get()) }
        viewModel { DetailViewModel(get(), get()) }
        viewModel { SearchViewModel(get(), get()) }
        viewModel { SettingViewModel(get()) }
    }

    val settingsModule = module {
        single { ThemeManager(androidContext()) }
    }

    fun getAppModules() = listOf(
        coroutineModule,
        loggingModule,
        chuckerModule,
        mapperModule,
        serviceModule,
        repositoryModule,
        featureModule,
        settingsModule
    )
}