/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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

package net.java.libuv;

import java.nio.ByteBuffer;

import net.java.libuv.cb.ProcessCallback;
import net.java.libuv.cb.StreamCallback;
import net.java.libuv.cb.UDPCallback;

public final class LoggingCallback implements StreamCallback, UDPCallback, ProcessCallback {

    private final String prefix;

    public LoggingCallback() {
        this.prefix = null;
    }

    public LoggingCallback(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void call(final Object[] args) throws Exception {
        System.out.print(prefix == null ? "" : prefix);
        if (args != null) {
            for (final Object arg : args) {
                if (arg instanceof ByteBuffer) {
                    final byte[] bytes = ((ByteBuffer) arg).array();
                    System.out.print(new String(bytes, "utf-8"));
                } else {
                    System.out.print(arg);
                }
                System.out.print(" ");
            }
        }
        System.out.println();
    }

}
