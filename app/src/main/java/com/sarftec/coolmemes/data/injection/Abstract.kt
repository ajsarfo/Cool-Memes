package com.sarftec.coolmemes.data.injection

import com.sarftec.coolmemes.data.cache.UriCache
import com.sarftec.coolmemes.data.repository.*
import com.sarftec.coolmemes.domain.repository.*
import com.sarftec.messiwallpapers.data.cache.UriCacheImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface Abstract {

    @Binds
    fun imageRepository(repository: ImageRepositoryImpl) : ImageRepository

    @Binds
    fun memeRepository(repository: MemeRepositoryImpl) : MemeRepository

    @Binds
    fun favoriteRepository(repository: FavoriteRepositoryImpl) : FavoriteRepository

    @Binds
    fun approveRepository(repositoryImpl: ApproveRepositoryImpl) : ApproveRepository

    @Binds
    fun viewUploadRepository(repository: ViewUploadRepositoryImpl) : ViewUploadRepository

    @Binds
    fun uriCache(cache: UriCacheImpl) : UriCache
}