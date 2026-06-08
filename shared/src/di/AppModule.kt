package com.charan.bingediary.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module


@Module
@ComponentScan("com.charan.bingediary")
class AppModule

@KoinApplication(modules = [AppModule::class])
class App
