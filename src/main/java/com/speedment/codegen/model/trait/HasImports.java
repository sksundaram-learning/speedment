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
package com.speedment.codegen.model.trait;

import com.speedment.annotation.Api;
import com.speedment.codegen.model.Import;
import java.util.List;

/**
 * A trait for models that contain {@link Import} components.
 * 
 * @author Emil Forslund
 * @param <T> The extending type
 * @since  2.0
 */
@Api(version = "2.3")
public interface HasImports<T extends HasImports<T>> {
    
    /**
     * Adds the specified {@link Import} to this model.
     * 
     * @param dep  the new child
     * @return     a reference to this
     */
    @SuppressWarnings("unchecked")
    default T add(final Import dep) {
        getImports().add(dep);
        return (T) this;
    }
    
    /**
     * Returns a list of all imports in this model.
     * <p>
     * The list returned must be mutable for changes!
     * 
     * @return  the imports
     */
    List<Import> getImports();
}