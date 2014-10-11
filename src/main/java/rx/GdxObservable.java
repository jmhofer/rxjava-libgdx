/**
 * Copyright 2014 Joachim Hofer & contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.physics.box2d.World;

import rx.functions.Action0;
import rx.functions.Func1;
import rx.libgdx.events.input.InputEvent;
import rx.libgdx.events.box2d.ContactEvent;
import rx.libgdx.events.lifecycle.LifecycleEvent;
import rx.libgdx.sources.GdxBox2DEventSource;
import rx.libgdx.sources.GdxInputEventSource;
import rx.libgdx.sources.GdxLifecycleEventSource;
import rx.subscriptions.Subscriptions;

/**
 * Allows creating observables from various sources specific to libgdx. 
 */
public enum GdxObservable { ; // no instances

    /**
     * Creates an observable corresponding to the game's input events.
     * Publish this and convert to the more specific input events you require.
     *
     * @return Observable emitting all input events.
     */
    public static Observable<InputEvent> fromInput() {
        return GdxInputEventSource.fromInput();
    }

    /**
     * Creates an observable corresponding to the game's physics contact events (using Box2D).
     * Publish this and convert to the more specific contact events you require.
     *
     * @param world The Box2D physics world to listen to.
     * @return Observable emitting all contact events.
     */
    public static Observable<ContactEvent> fromBox2DContact(World world) {
        return GdxBox2DEventSource.fromBox2DContact(world);
    }

    /**
     * Creates an observable corresponding to the game's lifecycle events.
     * Publish this and convert to the more specific contact events you require.
     *
     * @return Observable emitting all lifecycle events.
     */
    public static Observable<LifecycleEvent> fromLifecycle() {
        return GdxLifecycleEventSource.fromLifecycle();
    }

    /**
     * Creates an observable corresponding to the http request events.
     * Publish this and convert to the more specific contact events you require.
     *
     * @param httpRequest The request to execute on subscription
     * @return Observable emitting all lifecycle events.
     */
    public static Observable<Net.HttpResponse> fromHttpRequest(final Net.HttpRequest httpRequest) {
        return Observable.create(new Observable.OnSubscribe<Net.HttpResponse>() {

            @Override
            public void call(final Subscriber<? super Net.HttpResponse> subscriber) {
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {

                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        subscriber.onNext(httpResponse);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void failed(Throwable t) {
                        subscriber.onError(t);
                    }

                    @Override
                    public void cancelled() {
                        subscriber.unsubscribe();
                    }

                });

                subscriber.add(Subscriptions.create(new Action0() {

                    @Override
                    public void call() {
                        Gdx.net.cancelHttpRequest(httpRequest);
                    }

                }));
            }

        });
    }

    /**
     * Filters and casts an observable.
     *
     * @param source The source observable
     * @param clazz The filtered and casted class
     * @param <U> The source type
     * @param <T> The filtered and casted type
     * @return An observable with those events that match the class provided
     */
    public static <U, T extends U> Observable<T> filtered(Observable<? extends U> source, final Class<T> clazz) {
        return source.filter(new Func1<U, Boolean>() {

            @Override
            public Boolean call(U event) {
                return clazz.isInstance(event);
            }
        }).map(new Func1<U, T>() {

            @Override
            public T call(U event) {
                return clazz.cast(event);
            }
        });
    }

}
