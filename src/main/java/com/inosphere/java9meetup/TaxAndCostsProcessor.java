package com.inosphere.java9meetup;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class TaxAndCostsProcessor<T, K> extends SubmissionPublisher<K> implements Flow.Processor<T, K> {
    private final Function<T, K> function;
    private Flow.Subscription subscription;

    public TaxAndCostsProcessor(Function<T, K> function) {
        super();
        this.function = function;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        submit(function.apply(item));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {
        System.out.println("Completed Tax and Costs Processor");
        close();
    }
}
