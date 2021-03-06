/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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

package jdk.incubator.http.internal.frame;

import jdk.incubator.http.internal.common.ByteBufferReference;
import jdk.incubator.http.internal.common.Utils;

public class PushPromiseFrame extends HeaderFrame {

    private int padLength;
    private final int promisedStream;

    public static final int TYPE = 0x5;

    // Flags
    public static final int END_HEADERS = 0x4;
    public static final int PADDED = 0x8;

    public PushPromiseFrame(int streamid, int flags, int promisedStream, ByteBufferReference[] buffers, int padLength) {
        super(streamid, flags, buffers);
        this.promisedStream = promisedStream;
        if(padLength > 0 ) {
            setPadLength(padLength);
        }
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    int length() {
        return headerLength + ((flags & PADDED) != 0 ? 5 : 4);
    }

    @Override
    public String toString() {
        return super.toString() + " promisedStreamid: " + promisedStream
                + " headerLength: " + headerLength;
    }

    @Override
    public String flagAsString(int flag) {
        switch (flag) {
            case PADDED:
                return "PADDED";
            case END_HEADERS:
                return "END_HEADERS";
        }
        return super.flagAsString(flag);
    }

    public void setPadLength(int padLength) {
        this.padLength = padLength;
        flags |= PADDED;
    }

    public int getPadLength() {
        return padLength;
    }

    public int getPromisedStream() {
        return promisedStream;
    }

    @Override
    public boolean endHeaders() {
        return getFlag(END_HEADERS);
    }

}
