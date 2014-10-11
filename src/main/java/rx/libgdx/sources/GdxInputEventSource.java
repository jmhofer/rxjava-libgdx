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
package rx.libgdx.sources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.libgdx.events.input.*;
import rx.subscriptions.Subscriptions;

import static rx.GdxObservable.filtered;
import static rx.Observable.create;

public enum GdxInputEventSource {
    ; // no instances

    /**
     * @see rx.GdxObservable#fromInput
     */
    public static Observable<InputEvent> fromInput() {
        return create(new Observable.OnSubscribe<InputEvent>() {

            @Override
            public void call(final Subscriber<? super InputEvent> subscriber) {
                final InputProcessor processor = new InputProcessor() {

                    @Override
                    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new TouchUpEvent(screenX, screenY, pointer, button));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new TouchDownEvent(screenX, screenY, pointer, button));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean touchDragged(int screenX, int screenY, int pointer) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new TouchDraggedEvent(screenX, screenY, pointer));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean keyDown(int keycode) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new KeyDownEvent(keycode));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean keyUp(int keycode) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new KeyUpEvent(keycode));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean keyTyped(char character) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new KeyTypedEvent(character));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean mouseMoved(int screenX, int screenY) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new MouseMovedEvent(screenX, screenY));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean scrolled(int amount) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(new ScrolledEvent(amount));
                            return true;
                        }
                        return false;
                    }

                };

                final InputProcessor wrapped = Gdx.input.getInputProcessor();
                final InputMultiplexer im;
                if (wrapped instanceof InputMultiplexer) {
                    im = (InputMultiplexer) wrapped;
                } else if (wrapped != null) {
                    im = new InputMultiplexer(wrapped);
                } else {
                    im = new InputMultiplexer();
                }

                im.addProcessor(processor);

                subscriber.add(Subscriptions.create(new Action0() {

                    @Override
                    public void call() {
                        im.removeProcessor(processor);
                        if (wrapped != null && wrapped != im && im.size() == 1) {
                            Gdx.input.setInputProcessor(wrapped);
                        }
                    }

                }));

                Gdx.input.setInputProcessor(im);
            }

        });
    }

    /**
     * Returns all "Touch Up" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Touch Up" events.
     */
    public static Observable<TouchUpEvent> touchUp(Observable<? extends InputEvent> source) {
        return filtered(source, TouchUpEvent.class);
    }

    /**
     * Returns all "Touch Down" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Touch Down" events.
     */
    public static Observable<TouchDownEvent> touchDown(Observable<? extends InputEvent> source) {
        return filtered(source, TouchDownEvent.class);
    }

    /**
     * Returns all "Touch Dragged" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Touch Dragged" events.
     */
    public static Observable<TouchDraggedEvent> touchDragged(Observable<? extends InputEvent> source) {
        return filtered(source, TouchDraggedEvent.class);
    }

    /**
     * Returns all "Mouse Moved" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Mouse Moved" events.
     */
    public static Observable<MouseMovedEvent> mouseMoved(Observable<? extends InputEvent> source) {
        return filtered(source, MouseMovedEvent.class);
    }

    /**
     * Returns all "Scrolled" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Scrolled" events.
     */
    public static Observable<ScrolledEvent> scrolled(Observable<? extends InputEvent> source) {
        return filtered(source, ScrolledEvent.class);
    }

    /**
     * Returns all "Key Typed" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Key Typed" events.
     */
    public static Observable<KeyTypedEvent> keyTyped(Observable<? extends InputEvent> source) {
        return filtered(source, KeyTypedEvent.class);
    }

    /**
     * Returns all "Key Up" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Key Up" events.
     */
    public static Observable<KeyUpEvent> keyUp(Observable<? extends InputEvent> source) {
        return filtered(source, KeyUpEvent.class);
    }

    /**
     * Returns all "Key Down" events. Use this after publishing via {@link rx.GdxObservable#fromInput}.
     *
     * @param source The observable of input events to use as source.
     * @return An observable emitting "Key Down" events.
     */
    public static Observable<KeyDownEvent> keyDown(Observable<? extends InputEvent> source) {
        return filtered(source, KeyDownEvent.class);
    }

}
