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
package com.speedment.field;

import com.speedment.annotation.Api;
import com.speedment.field.trait.ComparableFieldTrait;
import com.speedment.field.trait.FieldTrait;
import com.speedment.field.trait.ReferenceFieldTrait;

/**
 * A field that implements the {@link ReferenceFieldTrait} and 
 * {@link ComparableFieldTrait}.
 *
 * @param <ENTITY>  the entity type
 * @param <D>       the database type
 * @param <V>       the field value type
 * 
 * @author  Per Minborg
 * @author  Emil Forslund
 * @since   2.2
 * 
 * @see    ReferenceFieldTrait
 * @see    ComparableFieldTrait
 */
@Api(version = "2.3")
public interface ComparableField<ENTITY, D, V extends Comparable<? super V>> extends
    FieldTrait, 
    ReferenceFieldTrait<ENTITY, D, V>,
    ComparableFieldTrait<ENTITY, D, V> {
}