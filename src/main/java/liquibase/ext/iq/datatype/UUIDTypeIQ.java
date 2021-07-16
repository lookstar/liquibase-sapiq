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

import java.util.Locale;

@DataTypeInfo(name = "uuid", aliases = { "uniqueidentifier", "java.util.UUID" }, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class UUIDTypeIQ extends UUIDType {
    
    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        //Cosine: refer to https://help.sap.com/viewer/a898e08b84f21015969fa437e89860c8/16.1.4.2/en-US/a51bffce84f210159a9a9283e5646eea.html
        //UNIQUEIDENTIFIER values are stored and returned as BINARY(16).
        //Because UNIQUEIDENTIFIER values are large, using UNSIGNED BIGINT or UNSIGNED INT identity columns instead of UNIQUEIDENTIFIER is more efficient, if you do not need cross database unique identifiers.
        return new DatabaseDataType("UNIQUEIDENTIFIER");
    }
    
    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.UUID;
    }
}
