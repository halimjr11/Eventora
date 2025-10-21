package com.halimjr11.eventora.di

import androidx.room.Room
import androidx.work.WorkManager
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.halimjr11.eventora.BuildConfig
import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.core.coroutines.impl.DefaultDispatcherProvider
import com.halimjr11.eventora.data.db.EventDatabase
import com.halimjr11.eventora.data.mapper.LocalDataMapper
import com.halimjr11.eventora.data.mapper.RemoteDataMapper
import com.halimjr11.eventora.data.mapper.impl.LocalDataMapperImpl
import com.halimjr11.eventora.data.mapper.impl.RemoteDataMapperImpl
import com.halimjr11.eventora.data.preferences.SharedPreferenceHelper
import com.halimjr11.eventora.data.preferences.impl.SharedPreferenceHelperImpl
import com.halimjr11.eventora.data.repository.EventLocalRepositoryImpl
import com.halimjr11.eventora.data.repository.EventRemoteRemoteRepositoryImpl
import com.halimjr11.eventora.data.service.EventService
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.domain.repository.EventRemoteRepository
import com.halimjr11.eventora.domain.usecase.GetUpcomingUseCase
import com.halimjr11.eventora.ui.helper.ThemeManager
import com.halimjr11.eventora.utils.Constants
import com.halimjr11.eventora.utils.Constants.LOCAL_NAME
import com.halimjr11.eventora.view.features.detail.DetailViewModel
import com.halimjr11.eventora.view.features.favorite.viewmodel.FavoriteViewModel
import com.halimjr11.eventora.view.features.finished.viewmodel.FinishedViewModel
import com.halimjr11.eventora.view.features.home.viewmodel.HomeViewModel
import com.halimjr11.eventora.view.features.main.viewmodel.MainViewModel
import com.halimjr11.eventora.view.features.search.viewmodel.SearchViewModel
import com.halimjr11.eventora.view.features.settings.viewmodel.SettingViewModel
import com.halimjr11.eventora.view.features.splash.viewmodel.SplashViewModel
import com.halimjr11.eventora.view.features.upcoming.viewmodel.UpcomingViewModel
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
        single<EventRemoteRepository> { EventRemoteRemoteRepositoryImpl(get(), get(), get()) }
        single<EventLocalRepository> { EventLocalRepositoryImpl(get(), get(), get(), get()) }
    }
    private val useCaseModule = module {
        single {
            GetUpcomingUseCase(get(), get())
        }
    }
    private val mapperModule = module {
        single<RemoteDataMapper> { RemoteDataMapperImpl(get()) }
        single<LocalDataMapper> { LocalDataMapperImpl() }
    }
    private val sharedPreferenceModule = module {
        single {
            androidContext().getSharedPreferences(LOCAL_NAME, android.content.Context.MODE_PRIVATE)
        }
        single<SharedPreferenceHelper> { SharedPreferenceHelperImpl(get()) }
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
        viewModel { UpcomingViewModel(get(), get()) }
        viewModel { FinishedViewModel(get(), get()) }
        viewModel { DetailViewModel(get(), get(), get()) }
        viewModel { SearchViewModel(get(), get()) }
        viewModel { SettingViewModel(get(), get()) }
        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { FavoriteViewModel(get()) }
        viewModel { SplashViewModel(get(), get()) }
    }

    val workerModule = module {
        single { WorkManager.getInstance(androidContext()) }
    }

    val settingsModule = module {
        single { ThemeManager(androidContext()) }
    }

    private val databaseModule = module {
        single {
            Room.databaseBuilder(
                get(),
                EventDatabase::class.java,
                Constants.DB_NAME
            ).fallbackToDestructiveMigration(dropAllTables = false).build()
        }

        single { get<EventDatabase>().eventDao() }
    }

    fun getAppModules() = listOf(
        coroutineModule,
        loggingModule,
        chuckerModule,
        mapperModule,
        sharedPreferenceModule,
        serviceModule,
        repositoryModule,
        workerModule,
        useCaseModule,
        featureModule,
        settingsModule,
        databaseModule
    )
}