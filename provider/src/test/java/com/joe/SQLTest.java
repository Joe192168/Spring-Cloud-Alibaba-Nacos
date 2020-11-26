package com.joe;

import com.github.mnadeem.TableNameParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLTest {

    public static void main(String[] args) throws Exception {
        Map<String,String> tabMap = new HashMap<>();
        //String sql = "select * from t_user as tt where 1=1";
        String sql = "SELECT   '1' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(DISTINCT OUTPATIENT_NO) 门诊量 FROM   F_OUTPATIENT_COSTS_RECORD foc,   T_OFFICE_PROPERTY tf,   T_USER_PROPERTY tu WHERE   foc.BILLING_OFFICE_ID = tf.id   AND foc.BILLING_PERSON_ID = tu.id   AND RECORD_PROPERTY = 4   AND foc.OCCURRENCE_TIME >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND foc.OCCURRENCE_TIME < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME UNION ALL SELECT   '2' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(DISTINCT OUTPATIENT_NO) 门诊量 FROM   F_OUTPATIENT_COSTS_RECORD foc,   T_OFFICE_PROPERTY tf,   T_USER_PROPERTY tu WHERE   foc.BILLING_OFFICE_ID = tf.id   AND foc.BILLING_PERSON_ID = tu.id   AND RECORD_PROPERTY in(1, 2)   AND foc.OCCURRENCE_TIME >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND foc.OCCURRENCE_TIME < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME UNION all SELECT   '3' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(DISTINCT OUTPATIENT_NO) 门诊量 FROM   F_OUTPATIENT_COSTS_RECORD foc,   T_OFFICE_PROPERTY tf,   T_USER_PROPERTY tu WHERE   foc.BILLING_OFFICE_ID = tf.id   AND foc.BILLING_PERSON_ID = tu.id   AND RECORD_PROPERTY in(1, 2)   AND exists (     sELECT       no     FROM       F_DRUG_DISPENSE_RECORD     WHERE       GIVEOUT_DRUG_TYPE = 1       AND record_state = 0   )   AND foc.OCCURRENCE_TIME >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND foc.OCCURRENCE_TIME < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME UNION ALL SELECT   '4' TYPE,   tf.OFFICE_NAME 科室,   tu.USER_NAME 医生,   count(fir.INP_SERIAL_NUMBER) 门诊量 FROM   f_inpatient_record as fir,   T_OFFICE_PROPERTY as tf,   T_USER_PROPERTY tu WHERE   fir.BEHOSP_OFFICE = tf.id(+)   AND fir.BEHOSP_DOCTOR = tu.ID(+)   AND fir.RECORD_STATE = 0   AND fir.INHOSP_DATE >= to_date('2019-11-07 00:00：00', 'yyyy-mm-dd hh24:mi:ss')   AND fir.INHOSP_DATE < to_date('2019-11-08 00:00：00', 'yyyy-mm-dd hh24:mi:ss') GROUP BY   tf.OFFICE_NAME,   tu.USER_NAME";
        //String sql = "select CASE substr(money, 0, 4)            WHEN '药库期初' THEN '1'            WHEN '药库入库' THEN '2'            WHEN '药库出库' THEN '3'         ELSE '4' END as type,        STOREHOUSE_ID 库房,        OFFICE_NAME 库房名称,        id 药品,        DRUG_NAME 药品名称,        CAL_month 月,        cal_year 年,        cal_date 天,        金额,        数量   from (select STOREHOUSE_ID,                OFFICE_NAME,                id,                DRUG_NAME,                CAL_month,                cal_year,                cal_date,                药库入库金额,                药库入库数量,                药库出库金额,                药库出库数量,                药库期初数量,                药库期初金额,                药库期末数量,                药库期末金额           from (select a.STOREHOUSE_ID，e.OFFICE_NAME,                        d.id,                        d.DRUG_NAME,                        c.CAL_month,                        c.cal_year,                        c.cal_date,                        sum(a.IN_STORE_BUY_MONEY) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, c.CAL_month, c.cal_year) 药库入库金额,                        sum(a.IN_STORE_AMOUNT) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, c.CAL_month, c.cal_year) 药库入库数量,                        sum(a.OUT_STORE_MONEY) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, c.CAL_month, c.cal_year) 药库出库金额,                        sum(a.OUT_STORE_AMOUNT) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, c.CAL_month, c.cal_year) 药库出库数量,                        FIRST_VALUE(a.BEGINNING_AMOUNT) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, cal_date, c.CAL_month, c.cal_year order by d.id, cal_date, BEGINNING_AMOUNT desc) 药库期初数量,                        FIRST_VALUE(a.BEGINNING_MONEY) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, cal_date, c.CAL_month, c.cal_year order by d.id, cal_date, BEGINNING_MONEY desc) 药库期初金额,                        last_VALUE(a.TERMINAL_AMOUNT) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, CAL_month, c.cal_year order by d.id, cal_date, TERMINAL_AMOUNT rows between unbounded preceding and unbounded following) 药库期末数量,                        last_VALUE(a.TERMINAL_money) over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, CAL_month, c.cal_year order by d.id, cal_date, TERMINAL_money rows between unbounded preceding and unbounded following) 药库期末金额,                        row_number() over(partition by a.STOREHOUSE_ID, e.OFFICE_NAME, d.id, d.DRUG_NAME, CAL_month, c.cal_year order by d.id, cal_date) rn                   from F_DRUG_INTO_OUT a                  inner join t_dates c                     on a.DATE_ID = c.ID                  inner join T_DRUG_SPECIFICATION B                     on a.TRADE_NAME_ID = b.id                  inner join T_DRUG_DIRECTORY d                     on b.DRUG_ID = d.id                  inner join T_OFFICE_PROPERTY e                     on a.storehouse_id = e.id                  where cal_year = '2018'                    )          where rn = 1) a UNPIVOT INCLUDE NULLS(金额 for money in(药库入库金额,                                                                药库出库金额,                                                                药库期初金额,                                                                药库期末金额)) UNPIVOT INCLUDE NULLS(数量 for amount in(药库入库数量,                                                                                                                 药库出库数量,                                                                                                                 药库期初数量,                                                                                                                 药库期末数量)) a where substr(amount, 0, 4) = substr(money, 0, 4)";
        Collection<String> tables = new TableNameParser(sql).tables();
        System.out.println("$$$$$$$$$$$$$$解析器获取所有表名称$$$$$$$$$$$$$$");
        tables.forEach(System.out::println);
        for (String tableName:tables){
            Pattern p = Pattern.compile("\\s+("+tableName+"+)(\\s+(?i)as\\s+(\\w*)|\\s+(\\w*))",Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(sql);
            while (m.find()) {
                if(StringUtils.isNoneBlank(m.group())){
                    if (m.group().toLowerCase().contains("where")){
                        String[] arr = StringUtils.split(m.group()," ");
                        //去重
                        if (!tabMap.containsKey(arr[0])){
                            tabMap.put(arr[0],arr[0]);
                        }
                    }else if(m.group().toLowerCase().contains("as")){
                        String[] arr = StringUtils.split(m.group()," ");
                        //去重
                        if (!tabMap.containsKey(arr[0])){
                            tabMap.put(arr[0],arr[2]);
                        }
                    }else{
                        String[] arr = StringUtils.split(m.group()," ");
                        //去重
                        if (!tabMap.containsKey(arr[0])){
                            if (arr.length>1){
                                tabMap.put(arr[0],arr[1]);
                            }else{
                                tabMap.put(arr[0],arr[0]);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("$$$$$$$$$$$$$$正则表达式获取表别名$$$$$$$$$$$$$$");
        tabMap.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });
    }
    
}