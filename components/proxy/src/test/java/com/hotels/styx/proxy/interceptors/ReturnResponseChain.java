/*
  Copyright (C) 2013-2018 Expedia Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.hotels.styx.proxy.interceptors;

import com.hotels.styx.api.Eventual;
import com.hotels.styx.api.FullHttpResponse;
import com.hotels.styx.api.HttpInterceptor;
import com.hotels.styx.api.HttpInterceptor.Chain;
import com.hotels.styx.api.HttpResponse;

import static java.nio.charset.StandardCharsets.UTF_8;
import com.hotels.styx.api.HttpRequest;
import com.hotels.styx.server.HttpInterceptorContext;

/**
 * A handler that return whatever response returned from the passed in handler.
 *
 */
public final class ReturnResponseChain implements Chain {
    private final HttpResponse response;
    private HttpInterceptor.Context context;

    private ReturnResponseChain(HttpResponse response, HttpInterceptor.Context context) {
        this.response = response;
        this.context = context;
    }

    public HttpInterceptor.Context context() {
        return context;
    }

    public static ReturnResponseChain returnsResponse(HttpResponse response) {
        return new ReturnResponseChain(response, HttpInterceptorContext.create());
    }

    public static ReturnResponseChain returnsResponse(HttpResponse response, HttpInterceptor.Context context) {
        return new ReturnResponseChain(response, context);
    }

    public static ReturnResponseChain returnsResponse(String response) {
        return returnsResponse(FullHttpResponse.response().body(response, UTF_8).build().toStreamingResponse());
    }

    public static ReturnResponseChain returnsResponse(HttpResponse.Builder builder) {
        return returnsResponse(builder.build());
    }

    @Override
    public Eventual<HttpResponse> proceed(HttpRequest request) {
        return Eventual.of(response);
    }
}
