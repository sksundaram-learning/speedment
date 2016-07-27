package com.speedment.runtime.internal.field;

import com.speedment.runtime.config.identifier.FieldIdentifier;
import com.speedment.runtime.config.mapper.TypeMapper;
import com.speedment.runtime.field.LongField;
import com.speedment.runtime.field.predicate.Inclusion;
import com.speedment.runtime.field.method.LongGetter;
import com.speedment.runtime.field.method.LongSetter;
import com.speedment.runtime.internal.field.comparator.LongFieldComparatorImpl;
import com.speedment.runtime.internal.field.predicate.longs.LongBetweenPredicate;
import com.speedment.runtime.internal.field.predicate.longs.LongEqualPredicate;
import com.speedment.runtime.internal.field.predicate.longs.LongGreaterOrEqualPredicate;
import com.speedment.runtime.internal.field.predicate.longs.LongGreaterThanPredicate;
import com.speedment.runtime.internal.field.predicate.longs.LongInPredicate;
import java.util.Comparator;
import java.util.Set;
import com.speedment.runtime.field.predicate.FieldPredicate;
import static java.util.Objects.requireNonNull;

/**
 *
 * @param <ENTITY>  the entity type
 * @param <D>       the database type
 * 
 * @author  Emil Forslund
 * @since   3.0.0
 */
public final class LongFieldImpl<ENTITY, D> implements LongField<ENTITY, D> {
    
    private final FieldIdentifier<ENTITY> identifier;
    private final LongGetter<ENTITY> getter;
    private final LongSetter<ENTITY> setter;
    private final TypeMapper<D, Long> typeMapper;
    private final boolean unique;

    public LongFieldImpl(
            FieldIdentifier<ENTITY> identifier,
            LongGetter<ENTITY> getter,
            LongSetter<ENTITY> setter,
            TypeMapper<D, Long> typeMapper,
            boolean unique) {
        
        this.identifier = requireNonNull(identifier);
        this.getter     = requireNonNull(getter);
        this.setter     = requireNonNull(setter);
        this.typeMapper = requireNonNull(typeMapper);
        this.unique     = unique;
    }
    
    /*****************************************************************/
    /*                           Getters                             */
    /*****************************************************************/
    
    @Override
    public FieldIdentifier<ENTITY> identifier() {
        return identifier;
    }
    
    @Override
    public LongSetter<ENTITY> setter() {
        return setter;
    }

    @Override
    public LongGetter<ENTITY> getter() {
        return getter;
    }

    @Override
    public TypeMapper<D, Long> typeMapper() {
        return typeMapper;
    }
    
    @Override
    public boolean isUnique() {
        return unique;
    }

    /*****************************************************************/
    /*                          Comparators                          */
    /*****************************************************************/
    
    @Override
    public Comparator<ENTITY> comparator() {
        return new LongFieldComparatorImpl<>(this);
    }

    @Override
    public Comparator<ENTITY> comparatorNullFieldsFirst() {
        return comparator();
    }

    @Override
    public Comparator<ENTITY> comparatorNullFieldsLast() {
        return comparator();
    }

    /*****************************************************************/
    /*                           Operators                           */
    /*****************************************************************/
    
    @Override
    public FieldPredicate<ENTITY> equal(Long value) {
        return new LongEqualPredicate<>(this, value);
    }

    @Override
    public FieldPredicate<ENTITY> greaterThan(Long value) {
        return new LongGreaterThanPredicate<>(this, value);
    }

    @Override
    public FieldPredicate<ENTITY> greaterOrEqual(Long value) {
        return new LongGreaterOrEqualPredicate<>(this, value);
    }

    @Override
    public FieldPredicate<ENTITY> between(Long start, Long end, Inclusion inclusion) {
        return new LongBetweenPredicate<>(this, start, end, inclusion);
    }

    @Override
    public FieldPredicate<ENTITY> in(Set<Long> values) {
        return new LongInPredicate<>(this, values);
    }
}