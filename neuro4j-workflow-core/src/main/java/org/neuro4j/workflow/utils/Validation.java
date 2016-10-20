package org.neuro4j.workflow.utils;

import java.util.function.Supplier;

public final class Validation {
    private Validation() {
        throw new AssertionError("No org.neuro4j.workflow.utils.Validation instances for you!");
    }
    
    public static <X extends Throwable> void requireNonNull(Object value, Supplier<? extends X> exceptionSupplier) throws X {
        if (value == null) {
        	throw exceptionSupplier.get();
        }
    }
}