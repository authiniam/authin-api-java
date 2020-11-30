package com.authin.iam.api.request;

import java.util.concurrent.*;

public interface Executable<T> {
    CompletableFuture<T> execute();
}
