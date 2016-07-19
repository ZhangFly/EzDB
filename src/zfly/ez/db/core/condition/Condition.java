package zfly.ez.db.core.condition;

import zfly.ez.db.model.Column;
import zfly.ez.db.model.Table;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询条件基类
 * Created by YFei on 16/6/6.
 */
public abstract class Condition {

    protected String sql;

    Condition(final String fmt, final Object... args) {
        sql = fmt == null ? "" : fmt;
        if (args != null)
            for (Object arg : args) {
                sql = sql.replaceFirst("\\$c", "'" + arg + "'");
            }
    }

    String parsePlaceholder(final Table table) throws SQLException {
        if (table == null) {
            throw new SQLException("No enough info to make condition!!");
        }

        final String regex = "(t\\.)?\\$([0-9]+)|(t\\.)";
        final String tableName = table.getName();
        final Matcher matcher = Pattern.compile(regex).matcher(sql);
        while (matcher.find()) {
            if (matcher.group(2) == null) {
                sql = matcher.replaceFirst(tableName + ".");
            } else {
                final int position = Integer.valueOf(matcher.group(2));
                if (position > table.getColumns().size() || position < 1) {
                    throw new SQLException("Placeholder was overflow!!");
                }
                final Column columnInfo = table.getColumns().get(position - 1);
                sql = matcher.replaceFirst(String.format("%s.%s", tableName, columnInfo.getName()));
            }
            matcher.reset(sql);
        }
        return sql;
    }

    public abstract String getCondition(final Table table) throws SQLException;

}
