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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Locale;

@DataTypeInfo(name = "blob", aliases = {"longblob", "longvarbinary", "java.sql.Types.BLOB", "java.sql.Types.LONGBLOB", 
                                        "java.sql.Types.LONGVARBINARY", "java.sql.Types.VARBINARY", "java.sql.Types.BINARY", 
                                        "varbinary", "binary", "image", "tinyblob", "mediumblob"}, 
    minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class BlobTypeIQ extends BlobType {

    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return new DatabaseDataType("LONG BINARY");
    }

    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.BLOB;
    }
}
