package liquibase.ext.iq.datatype;

import liquibase.datatype.core.*;
import liquibase.ext.iq.database.IQDatabase;

import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.exception.DatabaseException;
import liquibase.util.StringUtil;

@DataTypeInfo(name = "xml", aliases = { "xmltype", "java.sql.Types.SQLXML" }, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class XMLTypeIQ extends XMLType {
    
    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return new DatabaseDataType("TEXT");
    }
    
    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.CLOB;
    }
}
