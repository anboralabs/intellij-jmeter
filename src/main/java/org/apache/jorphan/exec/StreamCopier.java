/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jorphan.exec;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Thread that copies a stream in the background; closes both input and output streams.
 * @since 2.8
 */
class StreamCopier extends Thread {

    private static final Logger log = LoggerFactory.getLogger(StreamCopier.class);

    private final InputStream is;
    private final OutputStream os;

    /**
     * @param is {@link InputStream}
     * @param os {@link OutputStream}
     * @throws IOException if something goes wrong
     */
    StreamCopier(InputStream is, OutputStream os) throws IOException {
        this.is = is;
        this.os = os;
    }

    /**
     * @see Thread#run()
     */
    @Override
    public void run() {
        final boolean isSystemOutput = os.equals(System.out) || os.equals(System.err);
        try (OutputStream ignored = isSystemOutput ? null : os;
             InputStream ignored1 = is) {
            IOUtils.copyLarge(is, os);
        } catch (IOException e) {
            log.warn("Error writing stream", e);
        }
    }

}
