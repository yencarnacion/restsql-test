/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.core.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.sql.Types;

import org.junit.Test;
import org.restsql.core.BaseTestCase;
import org.restsql.core.Factory;
import org.restsql.core.SqlResource;
import org.restsql.core.SqlResourceException;
import org.restsql.core.SqlResourceMetaData;
import org.restsql.core.TableMetaData;
import org.restsql.core.TableMetaData.TableRole;

public class SqlResourceMetadataTest extends BaseTestCase {

	@Test
	public void testGetTables_HierManyToMany() throws SqlResourceException {
		final SqlResource sqlResource = Factory.getSqlResource("HierManyToMany");

		assertEquals(3, sqlResource.getMetaData().getTables().size());
		TableMetaData table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("actor"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("actor", table.getTableName());

		// Pks
		assertEquals(1, table.getPrimaryKeys().size());
		assertEquals("actor_id", table.getPrimaryKeys().get(0).getColumnName());

		// Columns
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 1, true, "sakila", "actor", "actor_id",
				"actor_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 2, false, "sakila", "actor", "first_name",
				"first_name", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 3, false, "sakila", "actor", "last_name",
				"last_name", Types.VARCHAR);

		// Child table
		table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("film", table.getTableName());

		// Pks
		assertEquals(1, table.getPrimaryKeys().size());
		assertEquals("film_id", table.getPrimaryKeys().get(0).getColumnName());

		// Columns
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 4, true, "sakila", "film", "film_id",
				"film_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 5, false, "sakila", "film", "title",
				"title", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 6, false, "sakila", "film", "release_year",
				"year", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.INTEGER : Types.DATE);

		// // Join table
		// table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film_actor"));
		// assertNotNull(table);
		// assertEquals("sakila", table.getDatabaseName());
		// assertEquals("film_actor", table.getTableName());
		//
		// // Pks
		// assertEquals(2, table.getPrimaryKeys().size());
		// assertEquals("actor_id", table.getPrimaryKeys().get(0).getColumnName());
		// assertEquals("film_id", table.getPrimaryKeys().get(1).getColumnName());
		//
		// // Columns
		// assertEquals(2, table.getColumns().size());
		// AssertionHelper.assertColumnMetaData(table.getColumns(), 7, true, "sakila", "film_actor", "film_id",
		// "film_id", Types.SMALLINT);
		// AssertionHelper.assertColumnMetaData(table.getColumns(), 0, true, "sakila", "film_actor", "actor_id",
		// "actor_id", "smallint(5) unsigned");
		// assertTrue(table.getColumns().get("actor_id").isNonqueriedForeignKey());
	}

	@Test
	public void testGetTables_MultiPK() throws SqlResourceException {
		final SqlResource sqlResource = Factory.getSqlResource("SingleTable_MultiPK");

		assertEquals(1, sqlResource.getMetaData().getTables().size());
		final TableMetaData table = sqlResource.getMetaData().getTableMap()
				.get(getQualifiedTableName("film_actor"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("film_actor", table.getTableName());

		// Pks
		assertEquals(2, table.getPrimaryKeys().size());
		assertEquals("actor_id", table.getPrimaryKeys().get(0).getColumnName());
		assertEquals("film_id", table.getPrimaryKeys().get(1).getColumnName());

		// Columns
		assertEquals(2, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 1, true, "sakila", "film_actor", "actor_id",
				"actorId", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 2, true, "sakila", "film_actor", "film_id",
				"film_id", Types.SMALLINT);
	}

	@Test
	public void testGetTables_FlatManyToOne() throws SqlResourceException {
		final SqlResource sqlResource = Factory.getSqlResource("FlatManyToOne");

		// Parent table
		assertEquals(2, sqlResource.getMetaData().getTables().size());
		TableMetaData table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("film", table.getTableName());
		assertEquals(1, table.getPrimaryKeys().size());

		// Primary keys
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 1, true, "sakila", "film",
				"film_id", "film_id", Types.SMALLINT);

		// Columns
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 1, true, "sakila", "film", "film_id",
				"film_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 2, false, "sakila", "film", "title",
				"title", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 3, false, "sakila", "film", "release_year",
				"year", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.INTEGER : Types.DATE);

		// Child table
		table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("language"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("language", table.getTableName());

		// Pks
		assertEquals(1, table.getPrimaryKeys().size());
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 4, true, "sakila", "language",
				"language_id", "language_id", Types.SMALLINT);

		// Columns
		assertEquals(2, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 4, true, "sakila", "language",
				"language_id", "language_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 5, false, "sakila", "language", "name",
				"name", Types.VARCHAR);
	}

	@Test
	public void testGetTables_SingleTable() throws SqlResourceException {
		final SqlResource sqlResource = Factory.getSqlResource("SingleTable");

		assertEquals(1, sqlResource.getMetaData().getTables().size());
		final TableMetaData table = sqlResource.getMetaData().getTableMap()
				.get(getQualifiedTableName("actor"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("actor", table.getTableName());

		// Pks
		assertEquals(1, table.getPrimaryKeys().size());
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 1, true, "sakila", "actor",
				"actor_id", "id", Types.SMALLINT);

		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 1, true, "sakila", "actor", "actor_id",
				"id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 2, false, "sakila", "actor", "first_name",
				"first_name", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 3, false, "sakila", "actor", "last_name",
				"surname", Types.VARCHAR);

	}

	@Test
	public void testGetTables_HierManyToManyExt() throws SqlResourceException {
		final SqlResource sqlResource = Factory.getSqlResource("HierManyToManyExt");
		SqlResourceMetaData metaData = ((SqlResourceImpl) sqlResource).getMetaData();

		assertTrue(sqlResource.getMetaData().isHierarchical());
		assertEquals(5, sqlResource.getMetaData().getTables().size());

		// Parent table
		TableMetaData table = sqlResource.getMetaData().getParent();
		assertNotNull(table);
		assertNotNull(sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("actor")));
		assertEquals(getQualifiedTableName("actor"), table.getQualifiedTableName());
		assertEquals(TableRole.Parent, table.getTableRole());

		// Parent primary keys
		assertEquals(1, table.getPrimaryKeys().size());
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 1, true, "sakila", "actor",
				"actor_id", "actor_id", Types.SMALLINT);

		// Parent columns
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 1, true, "sakila", "actor", "actor_id",
				"actor_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 2, false, "sakila", "actor", "first_name",
				"first_name", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 3, false, "sakila", "actor", "last_name",
				"last_name", Types.VARCHAR);
		assertEquals(TableRole.Parent, table.getColumns().get("first_name").getTableRole());

		// Parent extension
		table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("actor_genre"));
		assertNotNull(table);
		assertEquals(TableRole.ParentExtension, table.getTableRole());

		// Parent extension primary keys
		assertEquals(1, table.getPrimaryKeys().size());
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 4, true, "sakila", "actor_genre",
				"actor_genre_id", "actor_genre_id", Types.SMALLINT);

		// Parent extension columns
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 4, true, "sakila", "actor_genre",
				"actor_genre_id", "actor_genre_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 0, false, "sakila", "actor_genre",
				"actor_id", "actor_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 5, false, "sakila", "actor_genre", "name",
				"name", Types.VARCHAR);
		assertEquals(TableRole.ParentExtension, table.getColumns().get("actor_genre_id").getTableRole());

		
		// Child table
		table = sqlResource.getMetaData().getChild();
		assertNotNull(table);
		assertNotNull(sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film")));
		assertEquals(getQualifiedTableName("film"), table.getQualifiedTableName());
		assertEquals(TableRole.Child, table.getTableRole());

		// Child primary keys
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 6, true, "sakila", "film",
				"film_id", "film_id", Types.SMALLINT);

		// Child columns
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 6, true, "sakila", "film", "film_id",
				"film_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 7, false, "sakila", "film", "title",
				"title", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 8, false, "sakila", "film", "release_year",
				"year", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.INTEGER : Types.DATE);
		assertEquals(TableRole.Child, table.getColumns().get("film_id").getTableRole());

		// Child extension
		table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film_rating"));
		assertNotNull(table);
		assertEquals(TableRole.ChildExtension, table.getTableRole());

		// Child extension primary keys
		assertEquals(1, table.getPrimaryKeys().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 9, true, "sakila", "film_rating",
				"film_rating_id", "film_rating_id", Types.SMALLINT);

		// Child extension columns
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 9, true, "sakila", "film_rating",
				"film_rating_id", "film_rating_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 0, false, "sakila", "film_rating",
				"film_id", "film_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 10, false, "sakila", "film_rating", "stars",
				"stars", Types.SMALLINT);
		assertEquals(TableRole.ChildExtension, table.getColumns().get("stars").getTableRole());

		// Join table
		table = metaData.getJoin();
		assertNotNull(table);
		assertNotNull(sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film_actor")));
		assertEquals(getQualifiedTableName("film_actor"), table.getQualifiedTableName());
		assertEquals(TableRole.Join, table.getTableRole());
		assertEquals(3, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 0, false, "sakila", "film_actor", "film_id",
				"film_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 0, false, "sakila", "film_actor",
				"actor_id", "actor_id", Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 0, false, "sakila", "film_actor",
				"last_update", "last_update", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.NULL
						: Types.TIMESTAMP);
		assertEquals(TableRole.Join, table.getColumns().get("actor_id").getTableRole());
		
		// Special read column lists
		assertEquals(10, metaData.getAllReadColumns().size());
		assertEquals("actor_id", metaData.getAllReadColumns().get(0).getColumnLabel());
		assertEquals("first_name", metaData.getAllReadColumns().get(1).getColumnLabel());
		assertEquals("last_name", metaData.getAllReadColumns().get(2).getColumnLabel());
		assertEquals("actor_genre_id", metaData.getAllReadColumns().get(3).getColumnLabel());
		assertEquals("name", metaData.getAllReadColumns().get(4).getColumnLabel());
		assertEquals("film_id", metaData.getAllReadColumns().get(5).getColumnLabel());
		assertEquals("title", metaData.getAllReadColumns().get(6).getColumnLabel());
		assertEquals("year", metaData.getAllReadColumns().get(7).getColumnLabel());
		assertEquals("film_rating_id", metaData.getAllReadColumns().get(8).getColumnLabel());
		assertEquals("stars", metaData.getAllReadColumns().get(9).getColumnLabel());

		assertEquals(5, metaData.getParentReadColumns().size());
		assertEquals("actor_id", metaData.getParentReadColumns().get(0).getColumnLabel());
		assertEquals("first_name", metaData.getParentReadColumns().get(1).getColumnLabel());
		assertEquals("last_name", metaData.getParentReadColumns().get(2).getColumnLabel());
		assertEquals("actor_genre_id", metaData.getParentReadColumns().get(3).getColumnLabel());
		assertEquals("name", metaData.getParentReadColumns().get(4).getColumnLabel());

		assertEquals(5, metaData.getChildReadColumns().size());
		assertEquals("film_id", metaData.getChildReadColumns().get(0).getColumnLabel());
		assertEquals("title", metaData.getChildReadColumns().get(1).getColumnLabel());
		assertEquals("year", metaData.getChildReadColumns().get(2).getColumnLabel());
		assertEquals("film_rating_id", metaData.getChildReadColumns().get(3).getColumnLabel());
		assertEquals("stars", metaData.getChildReadColumns().get(4).getColumnLabel());
	}

	@Test
	public void testGetTables_SingleTableAliased() throws SqlResourceException {
		final SqlResource sqlResource = Factory.getSqlResource("SingleTableAliased");

		assertEquals(1, sqlResource.getMetaData().getTables().size());
		final TableMetaData table = sqlResource.getMetaData().getTableMap()
				.get(getQualifiedTableName("film"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("film", table.getTableName());
		assertEquals("movie", table.getTableAlias());

		// Pks
		assertEquals(1, table.getPrimaryKeys().size());
		assertEquals("film_id", table.getPrimaryKeys().get(0).getColumnName());

		// Columns
		assertEquals(5, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 1, true, "sakila", "film", "film_id", "id",
				Types.SMALLINT);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 2, false, "sakila", "film", "title",
				"title", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 3, false, "sakila", "film", "release_year",
				"year", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.INTEGER : Types.DATE);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 4, false, "sakila", "film", "description",
				"description", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.VARCHAR : Types.LONGVARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 5, false, "sakila", "film", "rating",
				"rating", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.OTHER : Types.CHAR);
	}

	@Test
	public void testGetTables_SingleTableSub() throws SqlResourceException {
		SqlResource sqlResource = Factory.getSqlResource("sub.SingleTable");

		assertEquals(1, sqlResource.getMetaData().getTables().size());
		TableMetaData table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("film", table.getTableName());
		assertEquals("pelicula", table.getTableAlias());

		sqlResource = Factory.getSqlResource("sub.sub.SingleTable");

		assertEquals(1, sqlResource.getMetaData().getTables().size());
		table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("language"));
		assertNotNull(table);
		assertEquals("sakila", table.getDatabaseName());
		assertEquals("language", table.getTableName());
		assertEquals("language", table.getTableAlias());
	}

	@Test
	public void testGetTables_HierOneToMany() throws SqlResourceException {
		SqlResource sqlResource = Factory.getSqlResource("HierOneToMany");
		assertTrue(sqlResource.getMetaData().isHierarchical());
		assertEquals(2, sqlResource.getMetaData().getTables().size());

		// Check out the parent
		TableMetaData table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("language"));
		assertNotNull(table);
		assertEquals(TableRole.Parent, table.getTableRole());

		// Parent primary keys
		assertEquals(1, table.getPrimaryKeys().size());
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 1, true, "sakila", "language",
				"language_id", "langId", Types.SMALLINT);

		// Parent columns
		assertEquals(2, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 1, true, "sakila", "language",
				"language_id", "langId", Types.SMALLINT);
		assertEquals("language.language_id", table.getColumns().get("langId").getQualifiedColumnName());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 2, false, "sakila", "language", "name",
				"langName", Types.VARCHAR);

		// Check out the child
		table = sqlResource.getMetaData().getTableMap().get(getQualifiedTableName("film"));
		assertNotNull(table);
		assertEquals(TableRole.Child, table.getTableRole());

		// Child primary keys
		assertEquals(1, table.getPrimaryKeys().size());
		assertEquals("movie", table.getTableAlias());
		AssertionHelper.assertColumnMetaData(table.getPrimaryKeys().get(0), 3, true, "sakila", "film",
				"film_id", "film_id", Types.SMALLINT);

		// Child columns
		assertEquals(4, table.getColumns().size());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 3, true, "sakila", "film", "film_id",
				"film_id", Types.SMALLINT);
		assertEquals("film.film_id", table.getColumns().get("film_id").getQualifiedColumnName());
		AssertionHelper.assertColumnMetaData(table.getColumns(), 4, false, "sakila", "film", "title",
				"title", Types.VARCHAR);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 5, false, "sakila", "film", "release_year",
				"year", (getDatabaseType() == DatabaseType.PostgreSql) ? Types.INTEGER : Types.DATE);
		AssertionHelper.assertColumnMetaData(table.getColumns(), 0, false, "sakila", "film", "language_id",
				"langId", Types.SMALLINT);
		assertTrue("is nonqueried foreign key", table.getColumns().get("langId").isNonqueriedForeignKey());
	}
}
