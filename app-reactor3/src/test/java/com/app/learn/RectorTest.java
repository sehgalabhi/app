package com.app.learn;


import junit.framework.TestCase;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RectorTest extends TestCase {

    public void testCreateFluxMono() {
        Flux<String> seq = Flux.just("foo", "bar", "foobar");

        List<String> strings = Arrays.asList("foo", "bar", "foobar");
        Flux<String> stringFlux = Flux.fromIterable(strings);

        Mono<String> empty = Mono.empty();
        Mono<String> foo = Mono.just("foo");

        Flux<Integer> range = Flux.range(3, 5);

        Disposable subscribe = seq.subscribe();
        System.out.println(subscribe.isDisposed());

        seq.subscribe(i -> System.out.println(i));

        Flux<Integer> testException = Flux.range(1, 4).map(i -> {
            if (i == 3) {
                throw new RuntimeException("test exception");
            }
            return i;
        });

        testException.subscribe(i -> System.out.println(i),
                error -> System.out.println("Error " + error.getMessage()));

        Flux<Integer> range1 = Flux.range(1, 4);
        range1.subscribe(i -> System.out.println(i),
                error -> System.out.println("Error " + error.getMessage()),
                () -> {
                    System.out.println("done");
                });


    }

    public void testCustomSubscriber() {
        SampleSubscriber<Integer> ss = new SampleSubscriber<Integer>();
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> {
                    System.out.println("Done");
                },
                s -> ss.request(10));
        ints.subscribe(ss);
    }

    public void testGenrateWithSynchronousSink() {
        Flux<Object> generate = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + "=" + 3 * state);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                });

        generate.subscribe(s -> System.out.println(s));
        Flux<Object> generateMutable = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    sink.next("3 x " + state + "=" + 3 * state.get());
                    if (state.get() == 10) {
                        sink.complete();
                    }
                    state.incrementAndGet();
                    return state;
                },
                state -> System.out.println(state
                ));
        generateMutable.subscribe(s -> System.out.println(s));

    }

    public void testCreate(){

        Flux.create(fluxSink ->   {

        }  );
    }


    public class SampleSubscriber<T> extends BaseSubscriber<T> {
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            System.out.println("Started");
            request(1);
        }

        @Override
        protected void hookOnNext(T value) {
            System.out.println(value);
            request(1);
        }
    }

    interface MyEventListener<T> {
        void onDataChunk(List<T> chunk);

        void onProcessComplete();
    }

}

