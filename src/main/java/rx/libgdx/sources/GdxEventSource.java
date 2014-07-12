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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.*;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.libgdx.events.*;
import rx.libgdx.events.box2d.*;

import static rx.Observable.create;

public enum GdxEventSource { ; // no instances

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
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "Begin Contact" events.
     */
    public static Observable<BeginContactEvent> beginContact(Observable<? extends ContactEvent> source) {
        return filtered(source, BeginContactEvent.class);
    }
    
    /**
     * Returns all "End Contact" events. Use this after publishing via {@link rx.GdxObservable#fromBox2DContact}.
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "End Contact" events.
     */
    public static Observable<EndContactEvent> endContact(Observable<? extends ContactEvent> source) {
        return filtered(source, EndContactEvent.class);
    }
    
    /**
     * Returns all "PreSolve" events. Use this after publishing via {@link rx.GdxObservable#fromBox2DContact}.
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "PreSolve" events.
     */
    public static Observable<PreSolveContactEvent> preSolve(Observable<? extends ContactEvent> source) {
        return filtered(source, PreSolveContactEvent.class);
    }
    
    /**
     * Returns all "PostSolve" events. Use this after publishing via {@link rx.GdxObservable#fromBox2DContact}.
     * @param source The observable of contact events to use as source.
     * @return An observable emitting "PostSolve" events.
     */
    public static Observable<PostSolveContactEvent> postSolve(Observable<? extends ContactEvent> source) {
        return filtered(source, PostSolveContactEvent.class);
    }
    
    /**
     * @see rx.GdxObservable#fromInput
     */
    public static Observable<InputEvent> fromInput() {
        return create(new Observable.OnSubscribe<InputEvent>() {
            @Override
            public void call(final Subscriber<? super InputEvent> subscriber) {
                final InputProcessor wrapped = Gdx.input.getInputProcessor();
                Gdx.app.getInput().setInputProcessor(new InputProcessor() {
                    @Override
                    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                        if (wrapped != null && wrapped.touchUp(screenX, screenY, pointer, button)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new TouchUpEvent(screenX, screenY, pointer, button));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                        if (wrapped != null && wrapped.touchDown(screenX, screenY, pointer, button)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new TouchDownEvent(screenX, screenY, pointer, button));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean touchDragged(int screenX, int screenY, int pointer) {
                        if (wrapped != null && wrapped.touchDragged(screenX, screenY, pointer)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new TouchDraggedEvent(screenX, screenY, pointer));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean keyDown(int keycode) {
                        if (wrapped != null && wrapped.keyDown(keycode)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new KeyDownEvent(keycode));
                            return true;
                        }
                        return false;
                    }
              
                    @Override
                    public boolean keyUp(int keycode) {
                        if (wrapped != null && wrapped.keyUp(keycode)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new KeyUpEvent(keycode));
                            return true;
                        }
                        return false;
                    }
              
                    @Override
                    public boolean keyTyped(char character) {
                        if (wrapped != null && wrapped.keyTyped(character)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new KeyTypedEvent(character));
                            return true;
                        }
                        return false;
                    }
              
                    @Override
                    public boolean mouseMoved(int screenX, int screenY) {
                        if (wrapped != null && wrapped.mouseMoved(screenX, screenY)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new MouseMovedEvent(screenX, screenY));
                            return true;
                        }
                        return false;
                    }
              
                    @Override
                    public boolean scrolled(int amount) {
                        if (wrapped != null && wrapped.scrolled(amount)) {
                            return true;
                        } else if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new ScrolledEvent(amount));
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    /**
     * Returns all "Touch Up" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<TouchUpEvent> touchUp(Observable<? extends InputEvent> source) {
        return filtered(source, TouchUpEvent.class);
    }
    
    /**
     * Returns all "Touch Down" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Touch Down" events.
     */
    public static Observable<TouchDownEvent> touchDown(Observable<? extends InputEvent> source) {
        return filtered(source, TouchDownEvent.class);
    }
  
    /**
     * Returns all "Touch Dragged" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Touch Dragged" events.
     */
    public static Observable<TouchDraggedEvent> touchDragged(Observable<? extends InputEvent> source) {
        return filtered(source, TouchDraggedEvent.class);
    }

    /**
     * Returns all "Mouse Moved" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Mouse Moved" events.
     */
    public static Observable<MouseMovedEvent> mouseMoved(Observable<? extends InputEvent> source) {
        return filtered(source, MouseMovedEvent.class);
    }

    /**
     * Returns all "Scrolled" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Scrolled" events.
     */
    public static Observable<ScrolledEvent> scrolled(Observable<? extends InputEvent> source) {
        return filtered(source, ScrolledEvent.class);
    }

    /**
     * Returns all "Key Typed" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Key Typed" events.
     */
    public static Observable<KeyTypedEvent> keyTyped(Observable<? extends InputEvent> source) {
        return filtered(source, KeyTypedEvent.class);
    }

    /**
     * Returns all "Key Up" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Key Up" events.
     */
    public static Observable<KeyUpEvent> keyUp(Observable<? extends InputEvent> source) {
        return filtered(source, KeyUpEvent.class);
    }

    /**
     * Returns all "Key Down" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Key Down" events.
     */
    public static Observable<KeyDownEvent> keyDown(Observable<? extends InputEvent> source) {
        return filtered(source, KeyDownEvent.class);
    }
  
    private static <U, T extends U> Observable<T> filtered(Observable<? extends U> source, final Class<T> clazz) {
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
