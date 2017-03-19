package io.restall.hittgv.self;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class SupplierUtils {
    private SupplierUtils() {
    }

    public static <T> Supplier<T> wrap(Callable<T> callable) {
        return () -> {
            try {
                return callable.call();
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}