package com.wty.app.library.data;

import android.text.TextUtils;
import com.wty.app.library.utils.AppLogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wty
 * @Description 查询语句拼接器
 **/
public class QueryBuilder {

        public List<String> sqlquery = new ArrayList<String>();

        public QueryBuilder(){
            sqlquery.clear();
        }

        /**
         * @Decription select *
         **/
        public QueryBuilder selectAll(){
            select("*");
            return this;
        }

        /**
         * @Decription select null
         **/
        public QueryBuilder selectNull(){
            select("null");
            return this;
        }

        /**
         * @Decription select column1,column2,column3
         * @param columnname 列名
         **/
        public QueryBuilder select(String... columnname){
            List<String> list = new ArrayList<String>();
            for(String column:columnname){
                list.add(column);
            }
            sqlquery.add(String.format(" select %s ", TextUtils.join(",", list)));
            return this;
        }

        /**
         * @Decription from tablename
         * @param tablename 表名
         **/
        public QueryBuilder from(String tablename){
            sqlquery.add(String.format(" from %s ", tablename));
            return this;
        }

        /**
         * @Decription as alias
         * @param alias 别名
         **/
        public QueryBuilder as(String alias){
            sqlquery.add(String.format(" as %s ",alias));
            return this;
        }

        /**
         * @Decription where
         **/
        public QueryBuilder whereExists(String clause){
            sqlquery.add(String.format(" where exists (%s) ",clause));
            return this;
        }

        /**
         * @Decription where
         **/
        public QueryBuilder whereNotExists(String clause){
           sqlquery.add(String.format(" where not exists (%s) ",clause));
           return this;
        }

        /**
         * @Decription where (clause)
         * @param clause 子句
         **/
        public QueryBuilder where(String clause){
            sqlquery.add(String.format(" where (%s) ",clause));
            return this;
        }

        /**
         * @Decription limit value
         * @param value 个数
         **/
        public QueryBuilder limit(int value){
            sqlquery.add(String.format(" limit %s ",value));
            return this;
        }

        /**
         * @Decription limit value
         * @param value 个数
         **/
        public QueryBuilder offset(int value){
            sqlquery.add(String.format(" offset %s ",value));
            return this;
        }

        /**
         * @Decription order by value
         * @param clauses xxx desc,xxx asc
         **/
        public QueryBuilder orderBy(String... clauses){
            List<String> list = new ArrayList<String>();
            for(String column:clauses){
                list.add(column);
            }
            sqlquery.add(String.format(" order by %s ",TextUtils.join(",",list)));
            return this;
        }

        /**
         * @Decription group by value
         * @param clauses
         **/
        public QueryBuilder groupBy(String... clauses){
            List<String> list = new ArrayList<String>();
            for(String column:clauses){
                list.add(column);
            }
            sqlquery.add(String.format(" group by %s ",TextUtils.join(",",list)));
            return this;
        }

        /**
         * @Decription left join tablename as alias on clauses
         * @param tablename 表名
         * @param alias 别名
         * @param clauses On子句
         **/
        public QueryBuilder leftJoin(String tablename,String alias,String clauses){
            //LEFT JOIN 关键字会从左表 (table_name1) 那里返回所有的行，即使在右表 (table_name2) 中没有匹配的行。
            sqlquery.add(String.format(" left join %s as %s on (%s) ",tablename,alias,clauses));
            return this;
        }

        /**
         * @Decription left join tablename on clauses
         * @param tablename 表名
         * @param clauses On子句
         **/
        public QueryBuilder leftJoin(String tablename,String clauses){
            sqlquery.add(String.format(" left join %s on (%s) ",tablename,clauses));
            return this;
        }

        /**
         * @Decription inner join tablename as alias on clauses
         * @param tablename 表名
         * @param alias 别名
         * @param clauses On子句
         **/
        public QueryBuilder innerJoin(String tablename,String alias,String clauses){
            //在表中存在至少一个匹配时，INNER JOIN 关键字返回行。
            sqlquery.add(String.format(" inner join %s as %s on (%s) ",tablename,alias,clauses));
            return this;
        }

        /**
         * @Decription inner join tablename on clauses
         * @param tablename 表名
         * @param clauses On子句
         **/
        public QueryBuilder innerJoin(String tablename,String clauses){
            sqlquery.add(String.format(" inner join %s on (%s)",tablename,clauses));
            return this;
        }

        /**
         * @Decription right join tablename as alias on clauses
         * @param tablename 表名
         * @param alias 别名
         * @param clauses On子句
         **/
        public QueryBuilder rightJoin(String tablename,String alias,String clauses){
            //关键字会右表 (table_name2) 那里返回所有的行，即使在左表 (table_name1) 中没有匹配的行。
            sqlquery.add(String.format(" right join %s as %s on (%s) ",tablename,alias,clauses));
            return this;
        }

        /**
         * @Decription right join tablename on clauses
         * @param tablename 表名
         * @param clauses On子句
         **/
        public QueryBuilder rightJoin(String tablename,String clauses){
            sqlquery.add(String.format(" right join %s on (%s)",tablename,clauses));
            return this;
        }

        /**
         * @Decription 生成sqlite语句
         **/
        public String build(){
            String result = TextUtils.join(" ",sqlquery);
            AppLogUtil.d(result);
            return result;
        }

}
