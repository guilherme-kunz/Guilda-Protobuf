package com.guilda.protobuf.ui;

import com.guilda.protobuf.data.repository.UserRepository;
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
public final class UserViewModel_Factory implements Factory<UserViewModel> {
  private final Provider<UserRepository> repositoryProvider;

  public UserViewModel_Factory(Provider<UserRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UserViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static UserViewModel_Factory create(Provider<UserRepository> repositoryProvider) {
    return new UserViewModel_Factory(repositoryProvider);
  }

  public static UserViewModel newInstance(UserRepository repository) {
    return new UserViewModel(repository);
  }
}
