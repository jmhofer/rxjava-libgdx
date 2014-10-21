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
package rx;

import com.badlogic.gdx.ApplicationListener;
import rx.libgdx.events.lifecycle.*;
import rx.subscriptions.Subscriptions;

import static rx.Observable.create;

public enum RxApplication {
    ; // no instances

    public static ApplicationListener app(RxGame game) {
        game.start(obsv());
        return app;
    }

    private static Observable<LifecycleEvent> obsv() {
        return create(new Observable.OnSubscribe<LifecycleEvent>() {
            @Override
            public void call(final Subscriber<? super LifecycleEvent> subscriber) {
                subscriber.add(Subscriptions.empty());
                RxApplication.subscriber = subscriber;
            }
        });
    }

    private static Subscriber<? super LifecycleEvent> subscriber = null;

    private static final ApplicationListener app = new ApplicationListener() {
        @Override
        public void create() {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.onNext(new CreateEvent());
            }
        }

        @Override
        public void resize(int width, int height) {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.onNext(new ResizeEvent(width, height));
            }
        }

        @Override
        public void render() {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.onNext(new RenderEvent());
            }
        }

        @Override
        public void pause() {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.onNext(new PauseEvent());
            }
        }

        @Override
        public void resume() {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.onNext(new ResumeEvent());
            }
        }

        @Override
        public void dispose() {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.onNext(new DisposeEvent());
                subscriber.unsubscribe();
            }
        }
    };
}
