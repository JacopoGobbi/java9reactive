package com.inosphere.java9meetup;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Function;

public class BusinessSubscriber<T> implements Flow.Subscriber<T> {
    private final Function<T, Integer> function;
    private final String id;
    public List<Integer> results = new LinkedList<>();
    private Flow.Subscription subscription;

    public BusinessSubscriber(Function<T, Integer> function, String id){
        this.function= function;
        this.id=id;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        Integer result = function.apply(item);
        results.add(result);
        System.out.format("%s: %d", id, result).println();
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println(id+ " complete");
    }
}
