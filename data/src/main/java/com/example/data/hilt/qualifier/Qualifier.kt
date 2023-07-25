package com.example.data.hilt.qualifier

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockApi