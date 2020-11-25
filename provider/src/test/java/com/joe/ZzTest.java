package com.joe;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ZzTest {

    /**
     * 表名解析
     */
    private static Pattern p = Pattern.compile(
            "\\s+from\\s+(\\w+)(\\s+|,)(\\w*)|\\s+join\\s+(\\w+)\\s+(\\w*)\\s+on",
            Pattern.CASE_INSENSITIVE);

    /**
     * 预处理sql
     * @param sql sql
     * @return 处理后sql
     * @throws Exception 异常
     */
    private static String preHandleSql(String sql,boolean removeBrackets) throws Exception {
        // 1. 统一换行符
        if (sql.contains("\r\n")) {
            sql = sql.replaceAll("\r\n", "\n");
        } else {
            sql = sql.replaceAll("\r", "\n");
        }
        // 2. 去掉注释
        String[] rows = sql.split("\n");
        StringBuilder sb = new StringBuilder(sql.length());
        for (String row : rows) {
            int indexOfComment = row.indexOf("--");
            if (indexOfComment == -1) {
                sb.append(row).append("\n");
            } else {
                sb.append(row, 0, indexOfComment).append("\n");
            }
        }
        if (removeBrackets) {
            sql = removeBrackets(sb);
        }
        // 4. 找到第一个select和from，其间的字段即是要查询的字段列表
        return sql.toLowerCase();
    }


    /**
     * 括号消除：消除括号和括号中包含的内容，非贪心模式
     * 例如：
     * 输入select (ssss(sadf)sdfsdf(sdssf(sssdf)dsssf)dsslf) as a, (asdfsdf) as b, (asdfsdf) as c from dsfdf
     *            a组  b组  a组    c组    d组  b组   c组    d组      m组    m组      x组     x组
     *            A组                                     A组      B组    B组      C组     C组
     * 去掉select (去           掉           部           分) as a, (去掉部分) as b, (去掉部分) as c from dsfdf
     * 输出select  as a,  as b,  as c from dsfdf
     *
     * @param str 消除前的字符串
     * @return 消除后的字符串
     * @throws Exception 括号不匹配
     */
    private static String removeBrackets(StringBuilder str) throws Exception {
        // 1. 收集括号组
        List<int[]> bracketsContainer = new ArrayList<>();
        collectBrackets(str, 0, bracketsContainer);
        if (bracketsContainer.isEmpty()) {
            return str.toString();
        }
        // 2. 消除括号组
        StringBuilder newStr = new StringBuilder(str.length());
        int groupSize = bracketsContainer.size();
        for (int i = 0; i < groupSize; i++) {
            int[] currentBrackets = bracketsContainer.get(i);
            if (i == 0) {
                // 刚到第一组
                newStr.append(str.subSequence(0, currentBrackets[0]));
            }
            if (i + 1 == groupSize) {
                // 已到最后一组
                newStr.append(str.subSequence(currentBrackets[1] + 1, str.length()));
            } else {
                // 未到最后一组
                int[] nextBrackets = bracketsContainer.get(i + 1);
                newStr.append(str.subSequence(currentBrackets[1] + 1, nextBrackets[0]));
            }
        }
        return newStr.toString();
    }

    /**
     * 收集括号组
     *
     * @param str 消除前的字符串
     * @param fromIndex 从哪开始找括号
     * @param bracketsContainer 括号组容器
     * @throws Exception 括号不匹配、括号嵌套层级过多
     */
    private static void collectBrackets(StringBuilder str, int fromIndex, List<int[]> bracketsContainer) throws Exception {
        int firstLeftBracket = str.indexOf("(", fromIndex + 1);
        int nextLeftBracket = firstLeftBracket;
        // SQL中不包含左括号时，直接返回
        if (firstLeftBracket == -1) {
            return;
        }
        // 括号层级（因为前面已经找到一个左括号，所以初始值为1）
        int level = 1;
        int nextRightBracket = str.indexOf(")", fromIndex + 1);
        if (nextRightBracket == -1) {
            throw new Exception("括号不匹配");
        }
        // 避免死循环
        int maxLevel = 1000;
        do {
            int tempLeftBracket = str.indexOf("(", nextLeftBracket + 1);
            if (tempLeftBracket == -1 || tempLeftBracket > nextRightBracket) {
                // 找不到下一个左括号或者下一个左括号已属于下一个括号组
                break;
            } else {
                nextLeftBracket = tempLeftBracket;
            }
            nextRightBracket = str.indexOf(")", nextRightBracket + 1);
            if (nextRightBracket == -1) {
                throw new Exception("括号不匹配");
            }
            level++;
        } while (level <= maxLevel);
        if (level >= maxLevel) {
            throw new Exception("括号嵌套层级过多");
        }
        // 把收集到的括号组放入容器
        bracketsContainer.add(new int[] {firstLeftBracket, nextRightBracket});
        // 递归
        collectBrackets(str, nextRightBracket, bracketsContainer);
    }


    /**
     * 解析sql中的表名
     * @param sql sql
     * @return 表名
     * @throws Exception 异常
     */
    public static List<String> parseSqlRefTables(String sql) throws Exception {
        List<String> tableNames = Lists.newArrayList();
        String newSql = StringUtils.replaceAll(preHandleSql(sql, false),"\n"," ");
        Matcher m = p.matcher(newSql);
        while (m.find()) {
            tableNames.add(m.group());
        }
        List<String> result = Lists.newArrayList();
        for (String tableName : tableNames) {
            String trimTableName = StringUtils.split(tableName, " ")[1].trim();
            if (trimTableName.contains(",")) {
                result.addAll(Arrays.asList(StringUtils.split(trimTableName, ",")));
            } else {
                result.add(trimTableName);
            }
        }
        return result.parallelStream().distinct().collect(Collectors.toList());
    }

    public static void main(String[] args) throws Exception {
        String data1 = "SELECT   '1' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(DISTINCT OUTPATIENT_NO) 门诊量 from   F_OUTPATIENT_COSTS_RECORD foc,   T_OFFICE_PROPERTY tf,   T_USER_PROPERTY tu where   foc.BILLING_OFFICE_ID = tf.id   AND foc.BILLING_PERSON_ID = tu.id   AND RECORD_PROPERTY = 4   AND foc.OCCURRENCE_TIME >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND foc.OCCURRENCE_TIME < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME UNION ALL SELECT   '2' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(DISTINCT OUTPATIENT_NO) 门诊量 FROM   F_OUTPATIENT_COSTS_RECORD foc,   T_OFFICE_PROPERTY tf,   T_USER_PROPERTY tu WHERE   foc.BILLING_OFFICE_ID = tf.id   AND foc.BILLING_PERSON_ID = tu.id   AND RECORD_PROPERTY in(1, 2)   AND foc.OCCURRENCE_TIME >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND foc.OCCURRENCE_TIME < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME UNION all SELECT   '3' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(DISTINCT OUTPATIENT_NO) 门诊量 FROM   F_OUTPATIENT_COSTS_RECORD foc,   T_OFFICE_PROPERTY tf,   T_USER_PROPERTY tu WHERE   foc.BILLING_OFFICE_ID = tf.id   AND foc.BILLING_PERSON_ID = tu.id   AND RECORD_PROPERTY in(1, 2)   AND exists (     sELECT       no     FROM       F_DRUG_DISPENSE_RECORD     WHERE       GIVEOUT_DRUG_TYPE = 1       AND record_state = 0   )   AND foc.OCCURRENCE_TIME >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND foc.OCCURRENCE_TIME < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME UNION ALL SELECT   '4' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(fir.INP_SERIAL_NUMBER) 门诊量 FROM   f_inpatient_record fir,   T_OFFICE_PROPERTY tf,   T_USER_PROPERTY tu WHERE   fir.BEHOSP_OFFICE = tf.id(+)   AND fir.BEHOSP_DOCTOR = tu.ID(+)   AND fir.RECORD_STATE = 0   AND fir.INHOSP_DATE >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND fir.INHOSP_DATE < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME";
        List<String> tableNames = parseSqlRefTables(data1);
        tableNames.forEach(System.out::println);
    }

}
