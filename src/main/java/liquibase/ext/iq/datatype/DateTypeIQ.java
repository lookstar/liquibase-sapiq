package liquibase.ext.iq.datatype;

import liquibase.datatype.core.*;
import liquibase.ext.iq.database.IQDatabase;

import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.statement.DatabaseFunction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@DataTypeInfo(name="date", aliases = {"java.sql.Types.DATE", "java.sql.Date"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class DateTypeIQ extends DateType {

    @Override
    public boolean supports(Database database) {
        return database instanceof IQDatabase;
    }

    @Override
    public String objectToSql(Object value, Database database) {
        if ((value == null) || "null".equals(value.toString().toLowerCase(Locale.US))) {
            return null;
        } else if (value instanceof DatabaseFunction) {
            return database.generateDatabaseFunctionValue((DatabaseFunction) value);
        } else if ("CURRENT_TIMESTAMP()".equals(value.toString())) {
              return database.getCurrentDateTimeFunction();
        } else if (value instanceof java.sql.Timestamp) {
            return database.getDateLiteral(((java.sql.Timestamp) value));
        } else if (value instanceof java.sql.Date) {
            return database.getDateLiteral(((java.sql.Date) value));
        } else if (value instanceof java.sql.Time) {
            return database.getDateLiteral(((java.sql.Time) value));
        } else if (value instanceof java.util.Date) {
            return database.getDateLiteral(((java.util.Date) value));
        } else {
            return "'"+((String) value).replaceAll("'","''")+"'";
        }
    }


    @Override
    public Object sqlToObject(String value, Database database) {
        if (zeroTime(value)) {
            return value;
        }
        try {
            DateFormat dateFormat = getDateFormat(database);

            if ((database instanceof OracleDatabase) && value.matches("to_date\\('\\d+\\-\\d+\\-\\d+', " +
                "'YYYY\\-MM\\-DD'\\)")) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                value = value.replaceFirst(".*?'", "").replaceFirst("',.*","");
            }

            return new java.sql.Date(dateFormat.parse(value.trim()).getTime());
        } catch (ParseException e) {
            return new DatabaseFunction(value);
        }
    }

    private boolean zeroTime(String stringVal) {
        return "".equals(stringVal.replace("-", "").replace(":", "").replace(" ", "").replace("0", ""));
    }

    protected DateFormat getDateFormat(Database database) {
        if (database instanceof OracleDatabase) {
            return new SimpleDateFormat("dd-MMM-yy");
        } else {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    }
}
