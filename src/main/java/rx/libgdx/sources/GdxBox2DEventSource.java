/**
 * Copyright 2013 Netflix, Inc.
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

import com.badlogic.gdx.physics.box2d.*;
import rx.Observable;
import rx.Subscriber;
import rx.libgdx.events.box2d.*;

import static rx.GdxObservable.filtered;
import static rx.Observable.create;

public enum GdxBox2DEventSource {
    ; // no instances

    /**
     * @see rx.GdxObservable#fromBox2DContact
     */
    public static Observable<ContactEvent> fromBox2DContact(final World world) {
        return create(new Observable.OnSubscribe<ContactEvent>() {

            @Override
            public void call(final Subscriber<? super ContactEvent> subscriber) {
                world.setContactListener(new ContactListener() {

                    @Override
                    public void beginContact(Contact contact) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new BeginContactEvent(contact));
                        }
                    }

                    @Override
                    public void endContact(Contact contact) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new EndContactEvent(contact));
                        }
                    }

                    @Override
                    public void preSolve(Contact contact, Manifold oldManifold) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new PreSolveContactEvent(contact, oldManifold));
                        }
                    }

                    @Override
                    public void postSolve(Contact contact, ContactImpulse impulse) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new PostSolveContactEvent(contact, impulse));
                        }
                    }
                });
            }
        });
    }

    /**
     * Returns all "Begin Contact" events. Use this after publishing via {@link rx.GdxObservable#fromBox2DContact}.
     *
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "Begin Contact" events.
     */
    public static Observable<BeginContactEvent> beginContact(Observable<? extends ContactEvent> source) {
        return filtered(source, BeginContactEvent.class);
    }

    /**
     * Returns all "End Contact" events. Use this after publishing via {@link rx.GdxObservable#fromBox2DContact}.
     *
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "End Contact" events.
     */
    public static Observable<EndContactEvent> endContact(Observable<? extends ContactEvent> source) {
        return filtered(source, EndContactEvent.class);
    }

    /**
     * Returns all "PreSolve" events. Use this after publishing via {@link rx.GdxObservable#fromBox2DContact}.
     *
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "PreSolve" events.
     */
    public static Observable<PreSolveContactEvent> preSolve(Observable<? extends ContactEvent> source) {
        return filtered(source, PreSolveContactEvent.class);
    }

    /**
     * Returns all "PostSolve" events. Use this after publishing via {@link rx.GdxObservable#fromBox2DContact}.
     *
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "PostSolve" events.
     */
    public static Observable<PostSolveContactEvent> postSolve(Observable<? extends ContactEvent> source) {
        return filtered(source, PostSolveContactEvent.class);
    }

}
