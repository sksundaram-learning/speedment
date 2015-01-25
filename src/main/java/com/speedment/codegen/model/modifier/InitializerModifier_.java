/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
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
package com.speedment.codegen.model.modifier;

import java.lang.reflect.Modifier;
import java.util.Set;

/**
 *
 * @author pemi
 */
public enum InitializerModifier_ implements Modifier_<InitializerModifier_> {

    STATIC(Modifier.STATIC);

    private final static StaticSupport<InitializerModifier_> support = new StaticSupport<>(values());

    private final int value;

    private InitializerModifier_(int value) {
        this.value = Modifier_.requireInValues(value, Modifier.interfaceModifiers());
    }

    @Override
    public int getValue() {
        return value;
    }

    public static InitializerModifier_ by(final String text) {
        return support.by(text);
    }

    public static Set<InitializerModifier_> of(final String text) {
        return support.of(text);
    }

    public static Set<InitializerModifier_> of(final int code) {
        return support.of(code);
    }

    public static Set<InitializerModifier_> of(final InitializerModifier_... classModifiers) {
        return support.of(classModifiers);
    }

}