/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.oracle.libuv.handles;

import java.util.concurrent.atomic.AtomicBoolean;

import com.oracle.libuv.cb.PollCallback;

public class PollHandle extends Handle {

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private PollCallback onPoll = null;

    static {
        _static_initialize();
    }

    public enum Event {
        // must match values in uv.h
        READABLE(1),
        WRITABLE(2);

        final int value;

        Event(final int value) {
            this.value = value;
        }
    }

    public void setPollCallback(final PollCallback callback) {
        onPoll = callback;
    }

    protected PollHandle(final LoopHandle loop, final int fd) {
        super(_new(loop.pointer(), fd), loop);
        _initialize(pointer);
    }

    protected PollHandle(final LoopHandle loop, final long winsock) {
        super(_new(loop.pointer(), winsock), loop);
        _initialize(pointer);
    }

    public int start(int events) {
        return _start(pointer, events);
    }

    public int stop() {
        return _stop(pointer);
    }

    public void close() {
        if (closed.compareAndSet(false, true)) {
            _close(pointer);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    private void callPoll(final int status, final int events) {
        if (onPoll != null) {loop.getCallbackHandler().handlePollCallback(onPoll, status, events);}
    }

    private static native long _new(final long loop, final int fd);

    private static native long _new(final long loop, final long winsock);

    private static native void _static_initialize();

    private native void _initialize(final long ptr);

    private native int _start(final long ptr, final int events);

    private native int _stop(final long ptr);

    private native void _close(final long ptr);

}
