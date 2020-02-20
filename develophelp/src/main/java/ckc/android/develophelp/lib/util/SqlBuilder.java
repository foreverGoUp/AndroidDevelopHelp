package ckc.android.develophelp.lib.util;

/**
 * Created by peter on 2018/3/29.
 */

public class SqlBuilder {

    private StringBuilder mSBuilder = new StringBuilder();
    private static final String SPACE = " ";
    private static final String SELECT = "select";
    private static final String AS = " as ";
    private static final String FROM = " from";
    private static final String WHERE = " where";
    private static final String AND = " and";
    private static final String NOT = " NOT";
    private static final String IS_NULL = " IS NULL";
    private static final String IS_NOT_NULL = " IS NOT NULL";
    private static final String DISTINCT = " distinct";
    private static final String ORDER_BY = " order by";
    private static final String DESC = " desc";
    private static final String COUNT_ALL = " count(*)";
    //符号
    private static final String COMMA = ",";
    private static final String IS = "=";

    /**
     * 拼接的最后一步，构建
     *
     * @return 拼接的结果
     */
    public String build() {
        return mSBuilder.toString();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 基本结构语句
    ///////////////////////////////////////////////////////////////////////////
    public SqlBuilder select() {
        mSBuilder.append(SELECT);
        return this;
    }

    public SqlBuilder from() {
        mSBuilder.append(FROM);
        return this;
    }

    public SqlBuilder from(String table) {
        mSBuilder.append(FROM);
        table(table);
        return this;
    }

    /**
     * 判断是否已拼接where,是则追加and,否则追加where
     */
    public SqlBuilder where() {
        String str = mSBuilder.toString();
        if (!str.contains(WHERE)) {
            mSBuilder.append(WHERE);
        } else {
            and();
        }
        return this;
    }

    /**
     * SELECT * FROM Persons
     * WHERE LastName IN ('Adams','Carter')
     */
    public SqlBuilder in(Object[] valueArr) {
        mSBuilder.append(" in (");
        int count = valueArr.length;
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                mSBuilder.append(",");
            }
            mSBuilder.append(valueArr[i]);
        }
        mSBuilder.append(")");
        return this;
    }

    public SqlBuilder orderBy() {
        mSBuilder.append(ORDER_BY);
        return this;
    }

    public SqlBuilder orderBy(String columnName) {
        mSBuilder.append(ORDER_BY);
        columnName(columnName);
        return this;
    }

    public SqlBuilder desc() {
        mSBuilder.append(DESC);
        return this;
    }

    public SqlBuilder distinct() {
        mSBuilder.append(DISTINCT);
        return this;
    }

    public SqlBuilder and() {
        mSBuilder.append(AND);
        return this;
    }

    public SqlBuilder as(String newColumn) {
        mSBuilder.append(AS).append(newColumn);
        return this;
    }

    public SqlBuilder not() {
        mSBuilder.append(NOT);
        return this;
    }

    public SqlBuilder isNotNull() {
        mSBuilder.append(IS_NOT_NULL);
        return this;
    }

    public SqlBuilder isNull() {
        mSBuilder.append(IS_NULL);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 函数
    ///////////////////////////////////////////////////////////////////////////
    public SqlBuilder count(String columnName) {
        mSBuilder.append(" count(").append(columnName).append(")");
        return this;
    }

    public SqlBuilder count() {
        mSBuilder.append(COUNT_ALL);
        return this;
    }


    ///////////////////////////////////////////////////////////////////////////
    // 数据
    ///////////////////////////////////////////////////////////////////////////
    public SqlBuilder table(String value) {
        mSBuilder.append(SPACE).append(value);
        return this;
    }

    public SqlBuilder columnName(String name) {
        mSBuilder.append(SPACE).append(name);
        return this;
    }

    public SqlBuilder appendColumnName(String name) {
        mSBuilder.append(COMMA).append(name);
        return this;
    }

    public SqlBuilder columnValue(String value) {
        mSBuilder.append("='" + value + "'");
        return this;
    }

    public SqlBuilder columnValue(Integer value) {
        mSBuilder.append(IS).append(value);
        return this;
    }
}
