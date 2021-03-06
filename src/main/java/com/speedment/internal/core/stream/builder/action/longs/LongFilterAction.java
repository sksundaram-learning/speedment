/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.internal.core.stream.builder.action.longs;

import static com.speedment.internal.core.stream.builder.action.StandardBasicAction.FILTER;
import com.speedment.stream.action.Action;
import static java.util.Objects.requireNonNull;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;

/**
 *
 * @author pemi
 */
public final class LongFilterAction extends Action<LongStream, LongStream> {

    public LongFilterAction(LongPredicate predicate) {
        super(s -> s.filter(requireNonNull(predicate)), LongStream.class, FILTER);
    }

}
