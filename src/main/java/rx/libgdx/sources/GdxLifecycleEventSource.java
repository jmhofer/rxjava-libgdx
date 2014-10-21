/*
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
package rx.libgdx.sources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.libgdx.events.lifecycle.*;
import rx.subscriptions.Subscriptions;

import static rx.GdxObservable.filtered;

public enum GdxLifecycleEventSource {
    ;

    public static Observable<LifecycleEvent> fromLifecycle() {
        return Observable.create(new Observable.OnSubscribe<LifecycleEvent>() {

            @Override
            public void call(final Subscriber<? super LifecycleEvent> subscriber) {
                final LifecycleListener listener = new LifecycleListener() {

                    @Override
                    public void pause() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new PauseEvent());
                        }
                    }

                    @Override
                    public void resume() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new ResumeEvent());
                        }
                    }

                    @Override
                    public void dispose() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new DisposeEvent());
                        }
                    }

                };

                subscriber.add(Subscriptions.create(new Action0() {

                    @Override
                    public void call() {
                        Gdx.app.removeLifecycleListener(listener);
                    }

                }));

                Gdx.app.addLifecycleListener(listener);
            }

        });
    }

    /**
     * Returns all "Pause" events. Use this after publishing via {@link rx.GdxObservable#fromLifecycle}.
     *
     * @param source The observable of lifecycle events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<PauseEvent> pause(Observable<? extends LifecycleEvent> source) {
        return filtered(source, PauseEvent.class);
    }

    /**
     * Returns all "Resume" events. Use this after publishing via {@link rx.GdxObservable#fromLifecycle}.
     *
     * @param source The observable of lifecycle events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<ResumeEvent> resume(Observable<? extends LifecycleEvent> source) {
        return filtered(source, ResumeEvent.class);
    }

    /**
     * Returns all "Dispose" events. Use this after publishing via {@link rx.GdxObservable#fromLifecycle}.
     *
     * @param source The observable of lifecycle events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<DisposeEvent> dispose(Observable<? extends LifecycleEvent> source) {
        return filtered(source, DisposeEvent.class);
    }

    /**
     * Returns all "Create" events.
     *
     * @param source The observable of lifecycle events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<CreateEvent> create(Observable<? extends LifecycleEvent> source) {
        return filtered(source, CreateEvent.class);
    }

    /**
     * Returns all "Render" events.
     *
     * @param source The observable of lifecycle events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<RenderEvent> render(Observable<? extends LifecycleEvent> source) {
        return filtered(source, RenderEvent.class);
    }

    /**
     * Returns all "Resize" events.
     *
     * @param source The observable of lifecycle events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<ResizeEvent> resize(Observable<? extends LifecycleEvent> source) {
        return filtered(source, ResizeEvent.class);
    }
}
