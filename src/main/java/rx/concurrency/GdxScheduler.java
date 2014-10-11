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
package rx.concurrency;

import com.badlogic.gdx.Gdx;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Executes work on the Gdx UI thread.
 * This scheduler should only be used with actions that execute quickly.
 */
public final class GdxScheduler extends Scheduler {

    private static final GdxScheduler INSTANCE = new GdxScheduler();

    public static GdxScheduler get() {
        return INSTANCE;
    }

    private GdxScheduler() {
        // hide from public access
    }

    @Override
    public Worker createWorker() {
        return worker;
    }

    private Worker worker = new Worker() {

        volatile boolean isUnsubscribed;

        @Override
        public Subscription schedule(final Action0 action) {
            if (isUnsubscribed) {
                return Subscriptions.empty();
            }

            final AtomicBoolean run = new AtomicBoolean(true);
            Gdx.app.postRunnable(new Runnable() {

                @Override
                public void run() {
                    if (!isUnsubscribed && run.get()) action.call();
                }

            });
            return Subscriptions.create(new Action0() {

                @Override
                public void call() {
                    run.set(false);
                }

            });
        }

        @Override
        public Subscription schedule(final Action0 action, final long delayTime, final TimeUnit unit) {
            if (isUnsubscribed) {
                return Subscriptions.empty();
            }

            final AtomicBoolean run = new AtomicBoolean(true);

            final long delayInMillis = unit.toMillis(delayTime);
            if (delayInMillis < 0) {
                throw new IllegalArgumentException("delay may not be negative (in milliseconds): " + delayInMillis);
            }

            final Thread sleeper = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(unit.toMillis(delayTime));
                        if (!isUnsubscribed && run.get()) schedule(action);
                    } catch (InterruptedException e) {
                        run.set(false);
                    }
                }
            }, "gdx-scheduler-sleeper");
            sleeper.start();

            return Subscriptions.create(new Action0() {

                @Override
                public void call() {
                    sleeper.interrupt();
                    run.set(false);
                }

            });
        }

        @Override
        public void unsubscribe() {
            isUnsubscribed = true;
        }

        @Override
        public boolean isUnsubscribed() {
            return isUnsubscribed;
        }
    };

}
