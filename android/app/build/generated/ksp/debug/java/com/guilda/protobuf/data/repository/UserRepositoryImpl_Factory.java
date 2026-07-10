package com.guilda.protobuf.data.repository;

import com.guilda.protobuf.data.remote.UserApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class UserRepositoryImpl_Factory implements Factory<UserRepositoryImpl> {
  private final Provider<UserApi> apiProvider;

  public UserRepositoryImpl_Factory(Provider<UserApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public UserRepositoryImpl get() {
    return newInstance(apiProvider.get());
  }

  public static UserRepositoryImpl_Factory create(Provider<UserApi> apiProvider) {
    return new UserRepositoryImpl_Factory(apiProvider);
  }

  public static UserRepositoryImpl newInstance(UserApi api) {
    return new UserRepositoryImpl(api);
  }
}
