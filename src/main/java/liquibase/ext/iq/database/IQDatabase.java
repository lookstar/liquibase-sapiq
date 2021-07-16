package liquibase.ext.iq.database;

import liquibase.CatalogAndSchema;
import liquibase.Scope;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.statement.DatabaseFunction;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.logging.LogFactory;
import liquibase.logging.Logger;
import liquibase.structure.DatabaseObject;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IQDatabase extends AbstractJdbcDatabase {

    public static final String PRODUCT_NAME = "SAP IQ";
    private Set<String> systemTables = new HashSet<>();
    private Set<String> systemViews = new HashSet<>();

    public IQDatabase() {
        setCurrentDateTimeFunction("CURRENT_TIMESTAMP");
        setObjectQuotingStrategy(ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS);

        super.unquotedObjectsAreUppercased = false;
        super.addReservedWords(getDefaultReservedWords());

        this.systemViews = getDefaultSystemViews();
        this.systemTables = getDefaultSystemTables();
    }

    @Override
    public String getShortName() {
        return "iq";
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return PRODUCT_NAME;
    }

    @Override
    public Integer getDefaultPort() {
        return Integer.valueOf(2638);
    }

    @Override
    public Set<String> getSystemTables() {
        return systemTables;
    }

    @Override
    public Set<String> getSystemViews() {
        return systemViews;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    @Override
    public boolean supportsDropTableCascadeConstraints() {
        return true;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return PRODUCT_NAME.equalsIgnoreCase(conn.getDatabaseProductName());
    }

    @Override
    public String getDefaultDriver(String url) {
        if (url.startsWith("jdbc:sybase:Tds")) { //Is it jdbc:sap? how to distinguish ASE and IQ? jdbc:sybase:Tds:localhost:2638
            return "com.sybase.jdbc4.jdbc.SybDriver"; //Is it com.sap.jdbc4.jdbc.SybDriver?
        }
        return null;
    }

    @Override
    public String getDefaultCatalogName() {
        return super.getDefaultCatalogName() == null ? null : super.getDefaultCatalogName().toUpperCase();
    }

    @Override
    public String getDefaultSchemaName() {
        return super.getDefaultSchemaName() == null ? null : super.getDefaultSchemaName().toUpperCase();
    }

    @Override
    public String getJdbcCatalogName(final CatalogAndSchema schema) {
        return super.getJdbcCatalogName(schema) == null ? null : super.getJdbcCatalogName(schema).toUpperCase();
    }

    @Override
    public String getJdbcSchemaName(final CatalogAndSchema schema) {
        return super.getJdbcSchemaName(schema) == null ? null : super.getJdbcSchemaName(schema).toUpperCase();
    }

    @Override
    public boolean supportsCatalogs() {
        return true;
    }

    @Override
    public boolean supportsCatalogInObjectName(Class<? extends DatabaseObject> type) {
        return false;
    }

    @Override
    public boolean supportsSequences() {
        return true;
    }

    @Override
    public String getDatabaseChangeLogTableName() {
        return super.getDatabaseChangeLogTableName().toUpperCase();
    }

    @Override
    public String getDatabaseChangeLogLockTableName() {
        return super.getDatabaseChangeLogLockTableName().toUpperCase();
    }

    @Override
    public boolean isSystemObject(DatabaseObject example) {
        return super.isSystemObject(example);
    }

    @Override
    public boolean supportsTablespaces() {
        return false;
    }

    public String getAutoIncrementClause(BigInteger startWith, BigInteger incrementBy) {
        if (startWith != null && incrementBy != null) {
            return " AUTOINCREMENT(" + startWith + "," + incrementBy + ") ";
        }
        return " AUTOINCREMENT ";
    }

    @Override
    public boolean supportsAutoIncrement() {
        return true;
    }

    @Override
    public String getAutoIncrementClause() {
        return "";
    }

    @Override
    public boolean generateAutoIncrementStartWith(BigInteger startWith) {
        return true;
    }

    @Override
    public boolean generateAutoIncrementBy(BigInteger incrementBy) {
        return true;
    }

    @Override
    public boolean supportsRestrictForeignKeys() {
        return true;
    }

    @Override
    protected String getConnectionSchemaName() {
        DatabaseConnection connection = getConnection();
        if (connection == null) {
            return null;
        }
        try {
            ResultSet resultSet = ((JdbcConnection) connection).createStatement().executeQuery("SELECT CURRENT_SCHEMA()");
            resultSet.next();
            String schema = resultSet.getString(1);
            return schema;
        } catch (Exception e) {
            Scope.getCurrentScope().getLog(getClass()).info("Error getting default schema", e);
        }
        return null;
    }

    @Override
    public boolean supportsSchemas() {
        return true;
    }

    private Set<String> getDefaultReservedWords() {
        /*
         * List taken from
         * https://help.sap.com/viewer/a898e08b84f21015969fa437e89860c8/16.1.4.2/en-US/
         * a4ec20ca84f2101599e4ca641dea6f76.html
         */

        Set<String> reservedWords = new HashSet<>();
        reservedWords.add("add");
        reservedWords.add("all");
        reservedWords.add("alter");
        reservedWords.add("and");
        reservedWords.add("any");
        reservedWords.add("array");
        reservedWords.add("as");
        reservedWords.add("asc");
        reservedWords.add("attach");
        reservedWords.add("backup");
        reservedWords.add("begin");
        reservedWords.add("between");
        reservedWords.add("bigint");
        reservedWords.add("binary");
        reservedWords.add("bit");
        reservedWords.add("bottom");
        reservedWords.add("break");
        reservedWords.add("by");
        reservedWords.add("call");
        reservedWords.add("capability");
        reservedWords.add("cascade");
        reservedWords.add("case");
        reservedWords.add("cast");
        reservedWords.add("char");
        reservedWords.add("char_convert");
        reservedWords.add("character");
        reservedWords.add("check");
        reservedWords.add("checkpoint");
        reservedWords.add("close");
        reservedWords.add("comment");
        reservedWords.add("commit");
        reservedWords.add("compressed");
        reservedWords.add("conflict");
        reservedWords.add("connect");
        reservedWords.add("constraint");
        reservedWords.add("contains");
        reservedWords.add("continue");
        reservedWords.add("convert");
        reservedWords.add("create");
        reservedWords.add("cross");
        reservedWords.add("cube");
        reservedWords.add("current");
        reservedWords.add("current_timestamp");
        reservedWords.add("current_user");
        reservedWords.add("cursor");
        reservedWords.add("date");
        reservedWords.add("datetimeoffset");
        reservedWords.add("dbspace");
        reservedWords.add("deallocate");
        reservedWords.add("dec");
        reservedWords.add("decimal");
        reservedWords.add("declare");
        reservedWords.add("default");
        reservedWords.add("delete");
        reservedWords.add("deleting");
        reservedWords.add("desc");
        reservedWords.add("detach");
        reservedWords.add("distinct");
        reservedWords.add("do");
        reservedWords.add("double");
        reservedWords.add("drop");
        reservedWords.add("dynamic");
        reservedWords.add("else");
        reservedWords.add("elseif");
        reservedWords.add("encrypted");
        reservedWords.add("end");
        reservedWords.add("endif");
        reservedWords.add("escape");
        reservedWords.add("except");
        reservedWords.add("exception");
        reservedWords.add("exec");
        reservedWords.add("execute");
        reservedWords.add("existing");
        reservedWords.add("exists");
        reservedWords.add("externlogin");
        reservedWords.add("fetch");
        reservedWords.add("first");
        reservedWords.add("float");
        reservedWords.add("for");
        reservedWords.add("force");
        reservedWords.add("foreign");
        reservedWords.add("forward");
        reservedWords.add("from");
        reservedWords.add("full");
        reservedWords.add("goto");
        reservedWords.add("grant");
        reservedWords.add("group");
        reservedWords.add("having");
        reservedWords.add("holdlock");
        reservedWords.add("identified");
        reservedWords.add("if");
        reservedWords.add("in");
        reservedWords.add("index");
        reservedWords.add("inner");
        reservedWords.add("inout");
        reservedWords.add("insensitive");
        reservedWords.add("insert");
        reservedWords.add("inserting");
        reservedWords.add("install");
        reservedWords.add("instead");
        reservedWords.add("int");
        reservedWords.add("integer");
        reservedWords.add("integrated");
        reservedWords.add("intersect");
        reservedWords.add("into");
        reservedWords.add("is");
        reservedWords.add("isolation");
        reservedWords.add("join");
        reservedWords.add("json");
        reservedWords.add("kerberos");
        reservedWords.add("key");
        reservedWords.add("lateral");
        reservedWords.add("left");
        reservedWords.add("like");
        reservedWords.add("limit");
        reservedWords.add("lock");
        reservedWords.add("login");
        reservedWords.add("long");
        reservedWords.add("match");
        reservedWords.add("membership");
        reservedWords.add("merge");
        reservedWords.add("message");
        reservedWords.add("mode");
        reservedWords.add("modify");
        reservedWords.add("natural");
        reservedWords.add("nchar");
        reservedWords.add("new");
        reservedWords.add("no");
        reservedWords.add("noholdlock");
        reservedWords.add("not");
        reservedWords.add("notify");
        reservedWords.add("null");
        reservedWords.add("numeric");
        reservedWords.add("nvarchar");
        reservedWords.add("of");
        reservedWords.add("off");
        reservedWords.add("on");
        reservedWords.add("open");
        reservedWords.add("openstring");
        reservedWords.add("openxml");
        reservedWords.add("option");
        reservedWords.add("options");
        reservedWords.add("or");
        reservedWords.add("order");
        reservedWords.add("others");
        reservedWords.add("out");
        reservedWords.add("outer");
        reservedWords.add("over");
        reservedWords.add("passthrough");
        reservedWords.add("precision");
        reservedWords.add("prepare");
        reservedWords.add("primary");
        reservedWords.add("print");
        reservedWords.add("privileges");
        reservedWords.add("proc");
        reservedWords.add("procedure");
        reservedWords.add("publication");
        reservedWords.add("raiserror");
        reservedWords.add("readtext");
        reservedWords.add("real");
        reservedWords.add("reference");
        reservedWords.add("references");
        reservedWords.add("refresh");
        reservedWords.add("release");
        reservedWords.add("remote");
        reservedWords.add("remove");
        reservedWords.add("rename");
        reservedWords.add("reorganize");
        reservedWords.add("resource");
        reservedWords.add("restore");
        reservedWords.add("restrict");
        reservedWords.add("return");
        reservedWords.add("revoke");
        reservedWords.add("right");
        reservedWords.add("rollback");
        reservedWords.add("rollup");
        reservedWords.add("row");
        reservedWords.add("rowtype");
        reservedWords.add("save");
        reservedWords.add("savepoint");
        reservedWords.add("scroll");
        reservedWords.add("select");
        reservedWords.add("sensitive");
        reservedWords.add("session");
        reservedWords.add("set");
        reservedWords.add("setuser");
        reservedWords.add("share");
        reservedWords.add("smallint");
        reservedWords.add("some");
        reservedWords.add("spatial");
        reservedWords.add("sqlcode");
        reservedWords.add("sqlstate");
        reservedWords.add("start");
        reservedWords.add("stop");
        reservedWords.add("subtrans");
        reservedWords.add("subtransaction");
        reservedWords.add("synchronize");
        reservedWords.add("table");
        reservedWords.add("temporary");
        reservedWords.add("then");
        reservedWords.add("time");
        reservedWords.add("timestamp");
        reservedWords.add("tinyint");
        reservedWords.add("to");
        reservedWords.add("top");
        reservedWords.add("tran");
        reservedWords.add("treat");
        reservedWords.add("trigger");
        reservedWords.add("truncate");
        reservedWords.add("tsequal");
        reservedWords.add("unbounded");
        reservedWords.add("union");
        reservedWords.add("unique");
        reservedWords.add("uniqueidentifier");
        reservedWords.add("unknown");
        reservedWords.add("unnest");
        reservedWords.add("unsigned");
        reservedWords.add("update");
        reservedWords.add("updating");
        reservedWords.add("user");
        reservedWords.add("using");
        reservedWords.add("validate");
        reservedWords.add("values");
        reservedWords.add("varbinary");
        reservedWords.add("varbit");
        reservedWords.add("varchar");
        reservedWords.add("variable");
        reservedWords.add("varray");
        reservedWords.add("varying");
        reservedWords.add("view");
        reservedWords.add("wait");
        reservedWords.add("waitfor");
        reservedWords.add("when");
        reservedWords.add("where");
        reservedWords.add("while");
        reservedWords.add("window");
        reservedWords.add("with");
        reservedWords.add("within");
        reservedWords.add("work");
        reservedWords.add("writetext");
        reservedWords.add("xml");

        return reservedWords;
    }

    private Set<String> getDefaultSystemTables() {
        /*
         * List taken from
         * https://help.sap.com/viewer/a898e08b84f21015969fa437e89860c8/16.1.4.2/en-US/
         * a5c62e3984f21015b984ce582a68f90b.html
         */
        Set<String> defaultSystemTables = new HashSet<>();

        defaultSystemTables.add("ISYSARTICLE");
        defaultSystemTables.add("ISYSARTICLECOL");
        defaultSystemTables.add("ISYSATTRIBUTE");
        defaultSystemTables.add("ISYSATTRIBUTENAME");
        defaultSystemTables.add("ISYSCAPABILITY");
        defaultSystemTables.add("ISYSCHECK");
        defaultSystemTables.add("ISYSCOLPERM");
        defaultSystemTables.add("ISYSCOLSTAT");
        defaultSystemTables.add("ISYSCONSTRAINT");
        defaultSystemTables.add("ISYSDBFILE");
        defaultSystemTables.add("ISYSDBSPACE");
        defaultSystemTables.add("ISYSDBSPACEPERM");
        defaultSystemTables.add("ISYSDEPENDENCY");
        defaultSystemTables.add("ISYSDOMAIN");
        defaultSystemTables.add("ISYSEVENT");
        defaultSystemTables.add("ISYSEXTERNENV");
        defaultSystemTables.add("ISYSEXTERNENVOBJECT");
        defaultSystemTables.add("ISYSEXTERNLOGIN");
        defaultSystemTables.add("ISYSFKEY");
        defaultSystemTables.add("ISYSGROUP");
        defaultSystemTables.add("ISYSHISTORY");
        defaultSystemTables.add("ISYSIDX");
        defaultSystemTables.add("ISYSIDXCOL");
        defaultSystemTables.add("ISYSIQBACKUPHISTORY");
        defaultSystemTables.add("ISYSIQBACKUPHISTORYDETAIL");
        defaultSystemTables.add("ISYSIQDBFILE");
        defaultSystemTables.add("ISYSIQDBSPACE");
        defaultSystemTables.add("ISYSIQIDX");
        defaultSystemTables.add("ISYSIQINFO");
        defaultSystemTables.add("ISYSIQLOGICALSERVER");
        defaultSystemTables.add("ISYSIQLOGINPOLICYLSINFO");
        defaultSystemTables.add("ISYSIQLSLOGINPOLICYOPTION");
        defaultSystemTables.add("ISYSIQLSMEMBER");
        defaultSystemTables.add("ISYSIQLSPOLICY");
        defaultSystemTables.add("ISYSIQLSPOLICYOPTION");
        defaultSystemTables.add("ISYSIQMPXSERVER");
        defaultSystemTables.add("ISYSIQMPXSERVERAGENT");
        defaultSystemTables.add("ISYSIQPARTITIONCOLUMN");
        defaultSystemTables.add("ISYSIQTAB");
        defaultSystemTables.add("ISYSIQTABCOL");
        defaultSystemTables.add("ISYSJAR");
        defaultSystemTables.add("ISYSJARCOMPONENT");
        defaultSystemTables.add("ISYSJAVACLASS");
        defaultSystemTables.add("ISYSLOGINMAP");
        defaultSystemTables.add("ISYSLOGINPOLICY");
        defaultSystemTables.add("ISYSLOGINPOLICYOPTION");
        defaultSystemTables.add("ISYSMVOPTION");
        defaultSystemTables.add("ISYSMVOPTIONNAME");
        defaultSystemTables.add("ISYSOBJECT");
        defaultSystemTables.add("ISYSOPTION");
        defaultSystemTables.add("ISYSOPTSTAT");
        defaultSystemTables.add("ISYSPARTITION");
        defaultSystemTables.add("ISYSPARTITIONKEY");
        defaultSystemTables.add("ISYSPARTITIONSCHEME");
        defaultSystemTables.add("ISYSPHYSIDX");
        defaultSystemTables.add("ISYSPROCEDURE");
        defaultSystemTables.add("ISYSPROCPARM");
        defaultSystemTables.add("ISYSPROCPERM");
        defaultSystemTables.add("ISYSPROXYTAB");
        defaultSystemTables.add("ISYSPUBLICATION");
        defaultSystemTables.add("ISYSREMARK");
        defaultSystemTables.add("ISYSREMOTEOPTION");
        defaultSystemTables.add("ISYSREMOTEOPTIONTYPE");
        defaultSystemTables.add("ISYSREMOTETYPE");
        defaultSystemTables.add("ISYSREMOTEUSER");
        defaultSystemTables.add("ISYSSCHEDULE");
        defaultSystemTables.add("ISYSSERVER");
        defaultSystemTables.add("ISYSSOURCE");
        defaultSystemTables.add("ISYSSQLSERVERTYPE");
        defaultSystemTables.add("ISYSSUBPARTITIONKEY");
        defaultSystemTables.add("ISYSSUBSCRIPTION");
        defaultSystemTables.add("ISYSSYNC");
        defaultSystemTables.add("ISYSSYNCPROFILE");
        defaultSystemTables.add("ISYSSYNCSCRIPT");
        defaultSystemTables.add("ISYSTAB");
        defaultSystemTables.add("ISYSTABCOL");
        defaultSystemTables.add("ISYSTABLEPERM");
        defaultSystemTables.add("ISYSTEXTCONFIG");
        defaultSystemTables.add("ISYSTEXTIDX");
        defaultSystemTables.add("ISYSTEXTIDXTAB");
        defaultSystemTables.add("ISYSTRIGGER");
        defaultSystemTables.add("ISYSTYPEMAP");
        defaultSystemTables.add("ISYSUSER");
        defaultSystemTables.add("ISYSUSERAUTHORITY");
        defaultSystemTables.add("ISYSUSERMESSAGE");
        defaultSystemTables.add("ISYSUSERTYPE");
        defaultSystemTables.add("ISYSVIEW");
        defaultSystemTables.add("ISYSWEBSERVICE");

        return defaultSystemTables;
    }

    private Set<String> getDefaultSystemViews() {
        /*
         * List taken from
         * https://help.sap.com/viewer/a898e08b84f21015969fa437e89860c8/16.1.4.2/en-US/
         * a5c72f6184f2101582c3f469947a3516.html
         */
        Set<String> defaultSystemViews = new HashSet<>();
        defaultSystemViews.add("GTSYSPERFCACHEPLAN");
        defaultSystemViews.add("GTSYSPERFCACHESTMT");
        defaultSystemViews.add("ST_GEOMETRY_COLUMNS");
        defaultSystemViews.add("ST_SPATIAL_REFERENCE_SYSTEMS");
        defaultSystemViews.add("ST_UNITS_OF_MEASURE");
        defaultSystemViews.add("SYSARTICLE");
        defaultSystemViews.add("SYSARTICLECOL");
        defaultSystemViews.add("SYSARTICLECOLS");
        defaultSystemViews.add("SYSARTICLES");
        defaultSystemViews.add("SYSCAPABILITIES");
        defaultSystemViews.add("SYSCAPABILITY");
        defaultSystemViews.add("SYSCAPABILITYNAME");
        defaultSystemViews.add("SYSCATALOG");
        defaultSystemViews.add("SYSCERTIFICATE");
        defaultSystemViews.add("SYSCOLLATION");
        defaultSystemViews.add("SYSCOLLATIONMAPPINGS");
        defaultSystemViews.add("SYSCOLPERM");
        defaultSystemViews.add("SYSCOLSTAT");
        defaultSystemViews.add("SYSCOLSTATS");
        defaultSystemViews.add("SYSCOLUMN");
        defaultSystemViews.add("SYSCOLUMNS");
        defaultSystemViews.add("SYSCOLUMNS ASE");
        defaultSystemViews.add("SYSCOMMENTS ASE");
        defaultSystemViews.add("SYSCONSTRAINT");
        defaultSystemViews.add("SYSDATABASEVARIABLE");
        defaultSystemViews.add("SYSDBFILE");
        defaultSystemViews.add("SYSDBSPACE");
        defaultSystemViews.add("SYSDBSPACEPERM");
        defaultSystemViews.add("SYSDEPENDENCY");
        defaultSystemViews.add("SYSDOMAIN");
        defaultSystemViews.add("SYSEVENT");
        defaultSystemViews.add("SYSEVENTTYPE");
        defaultSystemViews.add("SYSEXTERNENV");
        defaultSystemViews.add("SYSEXTERNENVOBJECT");
        defaultSystemViews.add("SYSEXTERNLOGIN");
        defaultSystemViews.add("SYSFILE");
        defaultSystemViews.add("SYSFKCOL");
        defaultSystemViews.add("SYSFKEY");
        defaultSystemViews.add("SYSFOREIGNKEY");
        defaultSystemViews.add("SYSFOREIGNKEYS");
        defaultSystemViews.add("SYSGROUP");
        defaultSystemViews.add("SYSGROUPS");
        defaultSystemViews.add("SYSHISTORY");
        defaultSystemViews.add("SYSIDX");
        defaultSystemViews.add("SYSIDXCOL");
        defaultSystemViews.add("SYSINDEX");
        defaultSystemViews.add("SYSINDEXES");
        defaultSystemViews.add("SYSINFO");
        defaultSystemViews.add("SYSIQBACKUPHISTORY");
        defaultSystemViews.add("SYSIQBACKUPHISTORYDETAIL");
        defaultSystemViews.add("SYSIQCOLUMN"); 
        defaultSystemViews.add("SYSIQDBFILE");
        defaultSystemViews.add("SYSIQDBSPACE");
        defaultSystemViews.add("SYSIQFILE"); 
        defaultSystemViews.add("SYSIQIDX");
        defaultSystemViews.add("SYSIQINFO");
        defaultSystemViews.add("SYSIQLOGICALSERVER");
        defaultSystemViews.add("SYSIQLOGINPOLICYLSINFO");
        defaultSystemViews.add("SYSIQLSLOGINPOLICIES");
        defaultSystemViews.add("SYSIQLSLOGINPOLICYOPTION");
        defaultSystemViews.add("SYSIQLSMEMBER");
        defaultSystemViews.add("SYSIQLSMEMBERS");
        defaultSystemViews.add("SYSIQLSPOLICY");
        defaultSystemViews.add("SYSIQLSPOLICYOPTION");
        defaultSystemViews.add("SYSIQMPXSERVER");
        defaultSystemViews.add("SYSIQMPXSERVERAGENT");
        defaultSystemViews.add("SYSIQOBJECTS ASE");
        defaultSystemViews.add("SYSIQPARTITIONCOLUMN");
        defaultSystemViews.add("SYSIQRVLOG");
        defaultSystemViews.add("SYSIQRLVMERGEHISTORY");
        defaultSystemViews.add("SYSIQTAB");
        defaultSystemViews.add("SYSIQTABCOL");
        defaultSystemViews.add("SYSIQTABLE");
        defaultSystemViews.add("SYSIQVINDEX ASE");
        defaultSystemViews.add("SYSIXCOL");
        defaultSystemViews.add("SYSJAR");
        defaultSystemViews.add("SYSJARCOMPONENT");
        defaultSystemViews.add("SYSJAVACLASS");
        defaultSystemViews.add("SYSLDAPSERVER");
        defaultSystemViews.add("SYSLOGINMAP");
        defaultSystemViews.add("SYSLOGINPOLICY");
        defaultSystemViews.add("SYSLOGINPOLICYOPTION");
        defaultSystemViews.add("SYSLOGINS ASE");
        defaultSystemViews.add("SYSMUTEXSEMAPHORE");
        defaultSystemViews.add("SYSMVOPTION");
        defaultSystemViews.add("SYSMVOPTIONNAME");
        defaultSystemViews.add("SYSOBJECT");
        defaultSystemViews.add("SYSOBJECTS ASE");
        defaultSystemViews.add("SYSOPTION");
        defaultSystemViews.add("SYSOPTIONS");
        defaultSystemViews.add("SYSOPTSTAT");
        defaultSystemViews.add("SYSPARTITION");
        defaultSystemViews.add("SYSPARTITIONKEY");
        defaultSystemViews.add("SYSPARTITIONS");
        defaultSystemViews.add("SYSPARTITIONSCHEME");
        defaultSystemViews.add("SYSPHYSIDX");
        defaultSystemViews.add("SYSPROCAUTH");
        defaultSystemViews.add("SYSPROCEDURE");
        defaultSystemViews.add("SYSPROCPARM");
        defaultSystemViews.add("SYSPROCPARMS");
        defaultSystemViews.add("SYSPROCPERM");
        defaultSystemViews.add("SYSPROCS");
        defaultSystemViews.add("SYSPROXYTAB");
        defaultSystemViews.add("SYSPUBLICATION");
        defaultSystemViews.add("SYSPUBLICATIONS");
        defaultSystemViews.add("SYSREMARK");
        defaultSystemViews.add("SYSREMOTEOPTION");
        defaultSystemViews.add("SYSREMOTEOPTION2");
        defaultSystemViews.add("SYSREMOTEOPTIONS");
        defaultSystemViews.add("SYSREMOTEOPTIONTYPE");
        defaultSystemViews.add("SYSREMOTETYPE");
        defaultSystemViews.add("SYSREMOTETYPES");
        defaultSystemViews.add("SYSREMOTEUSER");
        defaultSystemViews.add("SYSREMOTEUSERS");
        defaultSystemViews.add("SYSROLEGRANT");
        defaultSystemViews.add("SYSROLEGRANTEXT");
        defaultSystemViews.add("SYSROLEGRANTS");
        defaultSystemViews.add("SYSSCHEDULE");
        defaultSystemViews.add("SYSSERVER");
        defaultSystemViews.add("SYSSOURCE");
        defaultSystemViews.add("SYSSPATIALREFERENCESYSTEM");
        defaultSystemViews.add("SYSSQLSERVERTYPE");
        defaultSystemViews.add("SYSSUBPARTITIONKEY");
        defaultSystemViews.add("SYSSUBSCRIPTION");
        defaultSystemViews.add("SYSSUBSCRIPTIONS");
        defaultSystemViews.add("SYSSYNC");
        defaultSystemViews.add("SYSSYNC2");
        defaultSystemViews.add("SYSSYNCPUBLICATIONDEFAULTS");
        defaultSystemViews.add("SYSSYNCS");
        defaultSystemViews.add("SYSSYNCSCRIPT");
        defaultSystemViews.add("SYSSYNCSCRIPTS");
        defaultSystemViews.add("SYSSYNCSUBSCRIPTIONS");
        defaultSystemViews.add("SYSSYNCUSERS");
        defaultSystemViews.add("SYSTAB");
        defaultSystemViews.add("SYSTABAUTH");
        defaultSystemViews.add("SYSTABCOL");
        defaultSystemViews.add("SYSTABLE");
        defaultSystemViews.add("SYSTABLEPERM");
        defaultSystemViews.add("SYSTEXTCONFIG");
        defaultSystemViews.add("SYSTEXTIDX");
        defaultSystemViews.add("SYSTEXTIDXTAB");
        defaultSystemViews.add("SYSTRIGGER");
        defaultSystemViews.add("SYSTRIGGERS");
        defaultSystemViews.add("SYSTYPEMAP");
        defaultSystemViews.add("SYSTYPES ASE");
        defaultSystemViews.add("SYSUSER");
        defaultSystemViews.add("SYSUSERAUTH");
        defaultSystemViews.add("SYSUSERAUTHORITY");
        defaultSystemViews.add("SYSUSERLIST");
        defaultSystemViews.add("SYSUSERMESSAGE");
        defaultSystemViews.add("SYSUSEROPTIONS");
        defaultSystemViews.add("SYSUSERPERM");
        defaultSystemViews.add("SYSUSERPERMS");
        defaultSystemViews.add("SYSUSERS ASE");
        defaultSystemViews.add("SYSUSERTYPE");
        defaultSystemViews.add("SYSVIEW");
        defaultSystemViews.add("SYSVIEWS");
        defaultSystemViews.add("SYSWEBSERVICE");
        defaultSystemViews.add("Transact-SQL");

        return defaultSystemViews;
    }
}
