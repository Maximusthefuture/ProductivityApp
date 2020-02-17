package com.maximus.productivityappfinalproject.domain;

import io.reactivex.Single;

public interface UseCase<P, R> {

    Single<R> execute(P parameter);
}
