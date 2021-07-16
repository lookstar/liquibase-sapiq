package liquibase.ext.iq.datatype;

import liquibase.datatype.core.*;
import liquibase.ext.iq.database.IQDatabase;

import liquibase.Scope;
import liquibase.change.core.LoadDataChange;
import java.util.Locale;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.exception.DatabaseIncapableOfOperation;
import liquibase.util.StringUtil;
import liquibase.util.grammar.ParseException;


@DataTypeInfo(name = "timestamp", aliases = {"java.sql.Types.TIMESTAMP", "java.sql.Types.TIMESTAMP_WITH_TIMEZONE", "java.sql.Timestamp", "timestamptz"}, 
            minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class TimestampTypeIQ extends TimestampType {

    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return new DatabaseDataType(database.escapeDataTypeName("datetime"));
    }

    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.DATE;
    }


}
