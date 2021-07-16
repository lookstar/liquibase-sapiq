package liquibase.ext.iq.datatype;

import liquibase.datatype.core.*;
import liquibase.ext.iq.database.IQDatabase;

import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;

import java.math.BigInteger;
import java.util.Arrays;

@DataTypeInfo(name="varchar", aliases = {"java.sql.Types.VARCHAR", "java.lang.String", "varchar2", "character varying"}, 
                    minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class VarcharTypeIQ extends VarcharType {

    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return new DatabaseDataType("VARCHAR", getParameters());
    }
    
    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.STRING;
    }
}
