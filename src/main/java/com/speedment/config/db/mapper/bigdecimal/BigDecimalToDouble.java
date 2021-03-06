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
package com.speedment.config.db.mapper.bigdecimal;

import com.speedment.config.db.mapper.TypeMapper;
import java.math.BigDecimal;

/**
 *
 * @author Per Minborg
 */
public class BigDecimalToDouble implements TypeMapper<BigDecimal, Double> {

    @Override
    public Class<Double> getJavaType() {
        return Double.class;
    }

    @Override
    public Class<BigDecimal> getDatabaseType() {
        return BigDecimal.class;
    }

    @Override
    public Double toJavaType(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    @Override
    public BigDecimal toDatabaseType(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    @Override
    public boolean isIdentityMapper() {
        return false;
    }

    @Override
    public boolean isApproximation() {
        return true;
    }

}
