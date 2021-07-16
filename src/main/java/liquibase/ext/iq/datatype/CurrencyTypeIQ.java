package liquibase.ext.iq.datatype;

import liquibase.datatype.core.*;
import liquibase.ext.iq.database.IQDatabase;

import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.util.StringUtil;

import java.util.Locale;


@DataTypeInfo(name="currency", aliases = {"money", "smallmoney"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class CurrencyTypeIQ  extends CurrencyType {

    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.NUMERIC;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {   
        return new DatabaseDataType("MONEY");
    }
}
