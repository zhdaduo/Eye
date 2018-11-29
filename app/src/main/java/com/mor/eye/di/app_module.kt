package com.mor.eye.di

import android.arch.persistence.room.Room
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.WeatherRepositoryImpl
import com.mor.eye.repository.local.AppDatabase
import com.mor.eye.util.rx.ApplicationSchedulerProvider
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.view.detail.activity.AuthorDetailViewModel
import com.mor.eye.view.detail.activity.CategoriesDetailViewModel
import com.mor.eye.view.detail.activity.TagsDetailViewModel
import com.mor.eye.view.detail.activity.VideoDetailViewModel
import com.mor.eye.view.detail.fragment.AuthorDetailIndexViewModel
import com.mor.eye.view.detail.fragment.CategoriesDetailIndexViewModel
import com.mor.eye.view.detail.fragment.TagsDetailIndexViewModel
import com.mor.eye.view.gallery.DefaultSystemUiSwitch
import com.mor.eye.view.gallery.GalleryViewModel
import com.mor.eye.view.home.HomeViewModel
import com.mor.eye.view.home.category.CategoryViewModel
import com.mor.eye.view.home.commontab.CommonTabViewModel
import com.mor.eye.view.home.community.CommunityViewModel
import com.mor.eye.view.home.daily.DailyViewModel
import com.mor.eye.view.home.discovery.FindViewModel
import com.mor.eye.view.home.recommend.RecommendViewModel
import com.mor.eye.view.home.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val eyeModule = module {

    viewModel { CommonTabViewModel(get(), get()) }
    viewModel { FindViewModel(get(), get()) }
    viewModel { RecommendViewModel(get(), get()) }
    viewModel { DailyViewModel(get(), get()) }
    viewModel { CommunityViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { CategoryViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CategoriesDetailIndexViewModel(get(), get()) }
    viewModel { CategoriesDetailViewModel(get(), get()) }
    viewModel { AuthorDetailViewModel(get(), get()) }
    viewModel { AuthorDetailIndexViewModel(get(), get()) }
    viewModel { TagsDetailViewModel(get(), get()) }
    viewModel { TagsDetailIndexViewModel(get(), get()) }
    viewModel { VideoDetailViewModel(get(), get()) }
    viewModel { GalleryViewModel() }

    single<EyeRepository> { WeatherRepositoryImpl(get(), get(), get(), get()) }

    // Room Database
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "eye-db")
                .build()
    }

    single { DefaultSystemUiSwitch() }

    // Expose recentSearchDao directly
    single { get<AppDatabase>().recentSearchDao() }
    single { get<AppDatabase>().categoryOrderDao() }
    single { get<AppDatabase>().activeTabDao() }
}

val rxModule = module {
    single<SchedulerProvider> { ApplicationSchedulerProvider() }
}

val EyeApp = listOf(eyeModule, rxModule)