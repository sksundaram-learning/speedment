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
package com.speedment.internal.util.document;

import com.speedment.Speedment;
import com.speedment.config.Document;
import com.speedment.config.db.Column;
import com.speedment.config.db.Dbms;
import com.speedment.config.db.ForeignKey;
import com.speedment.config.db.ForeignKeyColumn;
import com.speedment.config.db.Index;
import com.speedment.config.db.IndexColumn;
import com.speedment.config.db.PrimaryKeyColumn;
import com.speedment.config.db.Project;
import com.speedment.config.db.Schema;
import com.speedment.config.db.Table;
import com.speedment.config.db.parameters.DbmsType;
import com.speedment.exception.SpeedmentException;
import com.speedment.field.FieldIdentifier;
import static com.speedment.util.StaticClassUtil.instanceNotAllowed;
import com.speedment.util.StreamComposition;
import java.util.Optional;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 */
public final class DocumentDbUtil {

    public static DbmsType dbmsTypeOf(Speedment speedment, Dbms dbms) {
        final String typeName = dbms.getTypeName();
        return speedment.getDbmsHandlerComponent().findByName(typeName)
            .orElseThrow(() -> new SpeedmentException(
                "Unable to find the database type "
                + typeName
                + ". The installed types are: "
                + speedment.getDbmsHandlerComponent().supportedDbmsTypes()
                    .map(DbmsType::getName)
                    .collect(joining(", "))
            ));
    }

    public static Stream<? extends Document> traverseOver(Project project) {
        return Stream.concat(project.dbmses(), project.dbmses().flatMap(DocumentDbUtil::traverseOver));
    }

    public static Stream<? extends Document> traverseOver(Dbms dbms) {
        return Stream.concat(dbms.schemas(), dbms.schemas().flatMap(DocumentDbUtil::traverseOver));
    }

    public static Stream<? extends Document> traverseOver(Schema schema) {
        return Stream.concat(schema.tables(), schema.tables().flatMap(DocumentDbUtil::traverseOver));
    }

    public static Stream<? extends Document> traverseOver(Table table) {
        return StreamComposition.concat(
                Stream.concat(table.columns(), table.columns().flatMap(DocumentDbUtil::traverseOver)),
                Stream.concat(table.primaryKeyColumns(), table.primaryKeyColumns().flatMap(DocumentDbUtil::traverseOver)),
                Stream.concat(table.indexes(), table.indexes().flatMap(DocumentDbUtil::traverseOver)),
                Stream.concat(table.foreignKeys(), table.foreignKeys().flatMap(DocumentDbUtil::traverseOver))
        );
    }

    public static Stream<? extends Document> traverseOver(Column column) {
        return Stream.empty();
    }

    public static Stream<? extends Document> traverseOver(PrimaryKeyColumn primaryKeyColumn) {
        return Stream.empty();
    }

    public static Stream<? extends Document> traverseOver(Index index) {
        return Stream.concat(index.indexColumns(), index.indexColumns().flatMap(DocumentDbUtil::traverseOver));
    }

    public static Stream<? extends Document> traverseOver(IndexColumn indexColumn) {
        return Stream.empty();
    }

    public static Stream<? extends Document> traverseOver(ForeignKey foreignKey) {
        return Stream.concat(foreignKey.foreignKeyColumns(), foreignKey.foreignKeyColumns().flatMap(DocumentDbUtil::traverseOver));
    }

    public static Stream<? extends Document> traverseOver(ForeignKeyColumn foreignKeyColumn) {
        return Stream.empty();
    }

    public static <T> Stream<T> traverseOver(Project project, Class<T> clazz) {
        if (Dbms.class.isAssignableFrom(clazz)) {
            return project.dbmses().map(clazz::cast);
        } else {
            return project.dbmses().flatMap(dbms -> traverseOver(dbms, clazz));
        }
    }

    public static <T> Stream<T> traverseOver(Dbms dbms, Class<T> clazz) {
        if (Schema.class.isAssignableFrom(clazz)) {
            return dbms.schemas().map(clazz::cast);
        } else {
            return dbms.schemas().flatMap(schema -> traverseOver(schema, clazz));
        }
    }

    public static <T> Stream<T> traverseOver(Schema schema, Class<T> clazz) {
        if (Table.class.isAssignableFrom(clazz)) {
            return schema.tables().map(clazz::cast);
        } else {
            return schema.tables().flatMap(table -> traverseOver(table, clazz));
        }
    }

    public static <T> Stream<T> traverseOver(Table table, Class<T> clazz) {
        if (Column.class.isAssignableFrom(clazz)) {
            return table.columns().map(clazz::cast);
        } else if (PrimaryKeyColumn.class.isAssignableFrom(clazz)) {
            return table.primaryKeyColumns().map(clazz::cast);
        } else if (Index.class.isAssignableFrom(clazz)) {
            return table.indexes().map(clazz::cast);
        } else if (ForeignKey.class.isAssignableFrom(clazz)) {
            return table.foreignKeys().map(clazz::cast);
        } else {
            final Stream.Builder<T> sb = Stream.builder();
            table.columns().flatMap(c -> traverseOver(c, clazz)).forEachOrdered(sb::accept);
            table.primaryKeyColumns().flatMap(c -> traverseOver(c, clazz)).forEachOrdered(sb::accept);
            table.indexes().flatMap(c -> traverseOver(c, clazz)).forEachOrdered(sb::accept);
            table.foreignKeys().flatMap(c -> traverseOver(c, clazz)).forEachOrdered(sb::accept);
            return sb.build();
        }
    }

    public static <T> Stream<T> traverseOver(Column column, Class<T> clazz) {
        return Stream.empty();
    }

    public static <T> Stream<T> traverseOver(PrimaryKeyColumn pkColumn, Class<T> clazz) {
        return Stream.empty();
    }

    public static <T> Stream<T> traverseOver(Index index, Class<T> clazz) {
        if (IndexColumn.class.isAssignableFrom(clazz)) {
            return index.indexColumns().map(clazz::cast);
        } else {
            return index.indexColumns().flatMap(ic -> traverseOver(ic, clazz));
        }
    }

    public static <T> Stream<T> traverseOver(IndexColumn indexColumn, Class<T> clazz) {
        return Stream.empty();
    }

    public static <T> Stream<T> traverseOver(ForeignKey fk, Class<T> clazz) {
        if (ForeignKeyColumn.class.isAssignableFrom(clazz)) {
            return fk.foreignKeyColumns().map(clazz::cast);
        } else {
            return fk.foreignKeyColumns().flatMap(fcc -> traverseOver(fcc, clazz));
        }
    }

    public static <T> Stream<T> traverseOver(ForeignKeyColumn foreignKeyColumn, Class<T> clazz) {
        return Stream.empty();
    }

    public static Stream<? extends Document> typedChildrenOf(Table table) {
        return StreamComposition.concat(
            table.columns().map(Document.class::cast),
            table.primaryKeyColumns().map(Document.class::cast),
            table.indexes().map(Document.class::cast),
            table.foreignKeys().map(Document.class::cast)
        );
    }
    
    /**
     * Determines the connection URL to use for the specified {@code Dbms} by 
     * first:
     * <ol>
     *      <li>checking if the {@code CONNECTION_URL} property is set;
     *      <li>otherwise, calculate it using the {@link DbmsType}.
     * </ol>
     * If the {@link DbmsType} can not be found by calling 
     * {@link DocumentDbUtil#dbmsTypeOf(Speedment, Dbms)}, a 
     * {@code SpeedmentException} will be thrown.
     * 
     * @param speedment            the speedment instance
     * @param dbms                 the database manager
     * @return                     the connection URL to use
     * @throws SpeedmentException  if the {@link DbmsType} couldn't be found
     */
    public static String findConnectionUrl(Speedment speedment, Dbms dbms) throws SpeedmentException {
        return dbms.getConnectionUrl()
            .orElseGet(() -> dbms.defaultConnectionUrl(speedment));
    }
    
    /**
     * Returns {@code true} if the specified {@link Column} represents a column
     * that can only hold unique values in the database.
     * 
     * @param column               the column
     * @return                     {@code true} if unique, else {@code false}
     * @throws SpeedmentException  if an index or PK could not be traversed
     */
    public static boolean isUnique(Column column) throws SpeedmentException {
        final Table table = column.getParentOrThrow();
        
        return
            table.indexes()
                .filter(i -> i.indexColumns().count() == 1)
                .filter(Index::isUnique)
                .filter(Index::isEnabled)
                .flatMap(Index::indexColumns)
                .map(IndexColumn::findColumn)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(col -> isSame(column, col))
            || (
                table.primaryKeyColumns().count() == 1 &&
                table.primaryKeyColumns()
                    .map(PrimaryKeyColumn::findColumn)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .anyMatch(col -> isSame(column, col))
            );
    }
    
    public static Column referencedColumn(Speedment speedment, FieldIdentifier<?> identifier) {
        return referencedColumn(speedment, identifier.dbmsName(), identifier.schemaName(), identifier.tableName(), identifier.columnName());
    }
    
    public static Table referencedTable(Speedment speedment, FieldIdentifier<?> identifier) {
        return referencedTable(speedment, identifier.dbmsName(), identifier.schemaName(), identifier.tableName());
    }
    
    public static Schema referencedSchema(Speedment speedment, FieldIdentifier<?> identifier) {
        return referencedSchema(speedment, identifier.dbmsName(), identifier.schemaName());
    }
    
    public static Dbms referencedDbms(Speedment speedment, FieldIdentifier<?> identifier) {
        return referencedDbms(speedment, identifier.dbmsName());
    }
    
    public static Column referencedColumn(Speedment speedment, String dbmsName, String schemaName, String tableName, String columnName) {
        return referencedTable(speedment, dbmsName, schemaName, tableName)
            .columns().filter(column -> columnName.equals(column.getName()))
            .findAny().orElseThrow(() -> new SpeedmentException(
                "Could not find referenced " + Column.class.getSimpleName() + 
                " with name '" + columnName + "'."
            ));
    }
    
    public static Table referencedTable(Speedment speedment, String dbmsName, String schemaName, String tableName) {
        return referencedSchema(speedment, dbmsName, schemaName)
            .tables().filter(table -> tableName.equals(table.getName()))
            .findAny().orElseThrow(() -> new SpeedmentException(
                "Could not find referenced " + Table.class.getSimpleName() + 
                " with name '" + tableName + "'."
            ));
    }
    
    public static Schema referencedSchema(Speedment speedment, String dbmsName, String schemaName) {
        return referencedDbms(speedment, dbmsName)
            .schemas().filter(schema -> schemaName.equals(schema.getName()))
            .findAny().orElseThrow(() -> new SpeedmentException(
                "Could not find referenced " + Schema.class.getSimpleName() + 
                " with name '" + schemaName + "'."
            ));
    }
    
    public static Dbms referencedDbms(Speedment speedment, String dbmsName) {
        return speedment.getProjectComponent().getProject()
            .dbmses().filter(dbms -> dbmsName.equals(dbms.getName()))
            .findAny().orElseThrow(() -> new SpeedmentException(
                "Could not find referenced " + Dbms.class.getSimpleName() + 
                " with name '" + dbmsName + "'."
            ));
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(Column first, Column second) {
        if (first.getName().equals(second.getName())) {
            final Table firstParent  = first.getParentOrThrow();
            final Table secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(IndexColumn first, IndexColumn second) {
        if (first.getName().equals(second.getName())) {
            final Index firstParent  = first.getParentOrThrow();
            final Index secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(Index first, Index second) {
        if (first.getName().equals(second.getName())) {
            final Table firstParent  = first.getParentOrThrow();
            final Table secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(PrimaryKeyColumn first, PrimaryKeyColumn second) {
        if (first.getName().equals(second.getName())) {
            final Table firstParent  = first.getParentOrThrow();
            final Table secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(ForeignKeyColumn first, ForeignKeyColumn second) {
        if (first.getName().equals(second.getName())) {
            final ForeignKey firstParent  = first.getParentOrThrow();
            final ForeignKey secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(ForeignKey first, ForeignKey second) {
        if (first.getName().equals(second.getName())) {
            final Table firstParent  = first.getParentOrThrow();
            final Table secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(Table first, Table second) {
        if (first.getName().equals(second.getName())) {
            final Schema firstParent  = first.getParentOrThrow();
            final Schema secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(Schema first, Schema second) {
        if (first.getName().equals(second.getName())) {
            final Dbms firstParent  = first.getParentOrThrow();
            final Dbms secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(Dbms first, Dbms second) {
        if (first.getName().equals(second.getName())) {
            final Project firstParent  = first.getParentOrThrow();
            final Project secondParent = second.getParentOrThrow();
            return isSame(firstParent, secondParent);
        } else return false;
    }
    
    /**
     * Returns {@code true} if the two specified documents represents the same
     * element in the database. Two documents are considered same if they have
     * the same name and type and their parents are considered same.
     * 
     * @param first   the first document
     * @param second  the second document
     * @return        {@code true} if same, else {@code false}
     */
    public static boolean isSame(Project first, Project second) {
        return first.getName().equals(second.getName());
    }

    /**
     * Utility classes should not be instantiated.
     */
    private DocumentDbUtil() {
        instanceNotAllowed(getClass());
    }
}
