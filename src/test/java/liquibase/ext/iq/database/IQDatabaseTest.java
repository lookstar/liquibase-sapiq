package liquibase.ext.iq.database;

import liquibase.ext.iq.helpers.SetUtils;
import liquibase.CatalogAndSchema;
import liquibase.database.jvm.JdbcConnection;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;

import static liquibase.servicelocator.PrioritizedService.PRIORITY_DATABASE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IQDatabaseTest {

    IQDatabase database;

    @Before
    public void setup() {
        database = new IQDatabase();
    }

    @Test
    public void testGetShortName() {
        assertEquals("iq", database.getShortName());
    }

    @Test
    public void testGetDefaultDatabaseProductName() {
        assertEquals("SAP IQ", database.getDefaultDatabaseProductName());
    }

    @Test
    public void testGetDefaultPort() {
        assertNull(database.getDefaultPort());
    }

    @Test
    public void testGetCurrentTimeFunction() {
        assertEquals("current_timestamp::timestamp_ntz", database.getCurrentDateTimeFunction());
    }

    @Test
    public void testGetSystemTables() {
        assertThat(SetUtils.equals(new HashSet<String>(), database.getSystemViews()), is(true));
    }

    @Test
    public void testGetSystemViews() {
        assertThat(SetUtils.equals(new HashSet<String>(), database.getSystemViews()), is(true));
    }

    @Test
    public void testGetPriority() {
        assertEquals(PRIORITY_DATABASE, database.getPriority());
    }

    @Test
    public void testSupportsInitiallyDeferrableColumns() {
        assertFalse(database.supportsInitiallyDeferrableColumns());
    }

    @Test
    public void testSupportsDropTableCascadeConstraints() {
        assertTrue(database.supportsDropTableCascadeConstraints());
    }

    @Test
    public void testIsCorrectDatabaseImplementation() throws Exception {
        JdbcConnection jdbcConnection = mock(JdbcConnection.class);
        when(jdbcConnection.getDatabaseProductName()).thenReturn("SapIQ");
        assertTrue(database.isCorrectDatabaseImplementation(jdbcConnection));
    }

    @Test
    public void testGetDefaultDriver() {
        assertEquals("net.snowflake.client.jdbc.SnowflakeDriver", database.getDefaultDriver("jdbc:snowflake:"));
        assertNull(database.getDefaultDriver("jdbc:wrong-name:"));
    }

    @Test
    public void testSupportsSchemas() {
        assertTrue(database.supportsSchemas());
    }

    @Test
    public void testSupportsCatalogs() {
        assertTrue(database.supportsCatalogs());
    }

    @Test
    public void testSupportsCatalogInObjectName() {
        assertFalse(database.supportsCatalogInObjectName(null));
    }

    @Test
    public void testSupportsSequences() {
        assertTrue(database.supportsSequences());
    }

    @Test
    public void testGetDatabaseChangeLogTableName() {
        assertEquals("DATABASECHANGELOG", database.getDatabaseChangeLogTableName());
    }

    @Test
    public void testGetDatabaseChangeLogLockTableName() {
        assertEquals("DATABASECHANGELOGLOCK", database.getDatabaseChangeLogLockTableName());
    }

    @Test
    public void testIsSystemObject() {
        assertFalse(database.isSystemObject(null));
    }

    @Test
    public void testSupportsTablespaces() {
        assertFalse(database.supportsTablespaces());
    }

    @Test
    public void testSupportsAutoIncrementClause() {
        assertTrue(database.supportsAutoIncrement());
    }

    @Test
    public void testGetAutoIncrementClause() {
        assertEquals("", database.getAutoIncrementClause());
        assertEquals(" AUTOINCREMENT ", database.getAutoIncrementClause(null, null));
        assertEquals(" AUTOINCREMENT(1,1) ", database.getAutoIncrementClause(new BigInteger("1"), new BigInteger("1")));
    }

    @Test
    public void testGenerateAutoIncrementStartWith() {
        assertTrue(database.generateAutoIncrementStartWith(new BigInteger("1")));
    }

    @Test
    public void testGenerateAutoIncrementBy() {
        assertTrue(database.generateAutoIncrementBy(new BigInteger("1")));
    }

    @Test
    public void testSupportsRestrictForeignKeys() {
        assertTrue(database.supportsRestrictForeignKeys());
    }

    @Test
    public void testIsReservedWord() {
//        database.addReservedWords(Arrays.asList("TABLE", "FROM", "INTO"));
        assertTrue(database.isReservedWord("table"));
    }

    @Test
    public void defaultCatalogNameIsUpperCase() throws Exception {
        JdbcConnection mock = mock(JdbcConnection.class);
        database.setConnection(mock);
        when(mock.getCatalog()).thenReturn("foo");

        assertEquals("FOO", database.getDefaultCatalogName());
    }

    @Test
    public void defaultCatalogNameIsNullWhenConnectionIsNull() throws Exception {
        assertNull(database.getDefaultCatalogName());
    }

    @Test
    public void defaultSchemaNameIsUpperCase() throws Exception {
        JdbcConnection jdbcConnection = mock(JdbcConnection.class);
        ResultSet resultSet = mock(ResultSet.class);
        Statement statement = mock(Statement.class);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn("foo");
        when(jdbcConnection.createStatement()).thenReturn(statement);

        database.setConnection(jdbcConnection);

        assertEquals("FOO", database.getDefaultSchemaName());
    }

    @Test
    public void defaultSchemaNameIsNullWhenConnectionIsNull() throws Exception {
        assertNull(database.getDefaultSchemaName());
    }

    @Test
    public void jdbcCatalogNameIsUpperCase() {
        assertEquals("CATALOG", database.getJdbcCatalogName(new CatalogAndSchema("catalog", "schema")));
    }

    @Test
    public void jdbcCatalogNameIsNullWhenCatalogAndSchemaAreNull() {
        assertNull(database.getJdbcCatalogName(new CatalogAndSchema(null, null)));
    }

    @Test
    public void jdbcSchemaNameIsUpperCase() {
        assertEquals("SCHEMA", database.getJdbcSchemaName(new CatalogAndSchema("catalog", "schema")));
    }

    @Test
    public void jdbcSchemaNameIsNullWhenCatalogAndSchemaAreNull() {
        assertNull(database.getJdbcSchemaName(new CatalogAndSchema(null, null)));
    }

}