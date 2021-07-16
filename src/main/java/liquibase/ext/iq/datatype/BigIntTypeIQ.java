package liquibase.ext.iq.datatype;

import liquibase.datatype.core.*;

import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.ext.iq.database.IQDatabase;

@DataTypeInfo(name="bigint", aliases = {"java.sql.Types.BIGINT", "java.math.BigInteger", "java.lang.Long", "integer8", "bigserial", "serial8", "int8"}, 
    minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class BigIntTypeIQ extends BigIntType {

    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return new DatabaseDataType("BIGINT");
    }
}
