package com.ruoyi.web.controller.sql;

import com.ruoyi.common.core.controller.BaseController;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Configuration
@RequestMapping("sql/zx")
public class SqlController extends BaseController {

   /* @Value("${spring.datasource.driverClassName}")
    private String driverClassName = "oracle.jdbc.driver.OracleDriver";

    @Value("${spring.datasource.druid.master.url}")
    private String url = "jdbc:oracle:thin:@20.200.89.10:1521:idealdbs";

    @Value("${spring.datasource.druid.master.username}")
    private String username = "u1";

    @Value("${spring.datasource.druid.master.password}")
    private String password = "u1";*/

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SqlController.class);

    //执行sql
    private String prefix_zx = "sql/zx";

    //地址
    @Value("${spring.datasource.driverClassName}")
    private String sql_driver;
    @Value("${spring.datasource.druid.master.url}")
    private String sql_cx;
    @Value("${spring.datasource.druid.master.username}")
    private String name;
    @Value("${spring.datasource.druid.master.password}")
    private String word;


    //去到sql执行页面
    @GetMapping()
    public String sql(ModelMap mmap)
    {
        return prefix_zx + "/sql";
    }

   /* public static String decrypt(String info) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(word);
        encryptor.setAlgorithm("PBEWITHMD5ANDDES");
        return encryptor.decrypt(info);
    }*/

    public String decrypt(String info) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("HT73MFJjc/Micp1AquSDVg==");
        encryptor.setAlgorithm("PBEWITHMD5ANDDES");
        return encryptor.decrypt(info);
    }

    //执行结果
    @ResponseBody
    @RequestMapping(value = "transactionFlow/informationQuery1", method = RequestMethod.POST)
    public Map<String, Object> informationQuery1(@ModelAttribute("form") @RequestParam (value = "sql") String sql, Model model) throws SQLException {
        Map<String, Object> map =new HashMap<String, Object>();
        ResultSet rs= null;
        Connection conn=null;
        PreparedStatement ps = null;
        Map<String,String> headMap = new HashMap<String,String>();
        StringBuilder sblData=new StringBuilder();
        try {
            Class.forName(sql_driver).newInstance();

            //从配置文件里拿数据库路径
            String url = sql_cx;

            //orcl为数据库的SID
            //连接数据的用户名和密码
            String username = name;
            String password = word;

            conn= DriverManager.getConnection(url,username,password);
            conn.setAutoCommit(false);
            //获取statement  只能查询一次 一般使用PreparedStatement
            // 1---更新操作
            sql=sql.trim();
            String[] array=sql.split(";;");
            	for (int m = 0; m < array.length; m++) {
            		try {
            			sql= array[m];
            			sblData.append("START......").append("\n");
            		if(sql.startsWith("update ") || sql.startsWith("UPDATE ")|| sql.startsWith("\nUPDATE ")){
                        ps=conn.prepareStatement(sql);
                        int  rows=ps.executeUpdate();
                        sblData.append("\n").append("up "+rows+" data.").append("\n");
                    }else   if(sql.startsWith("delete ") || sql.startsWith("DELETE ")|| sql.startsWith("\nDELETE ")){
                        // 2--删除操作
                            ps=conn.prepareStatement(sql);
                            int  rows=ps.executeUpdate();
                            sblData.append("\n").append("de "+rows+" data.").append("\n");
                            // 3---新增操作
                    }else   if(sql.startsWith("insert ") || sql.startsWith("INSERT ")|| sql.startsWith("\nINSERT ")){
                        ps=conn.prepareStatement(sql);
                        int  rows=ps.executeUpdate();
                        sblData.append("\n").append("add "+rows+" data.").append("\n");
                    }else  if(sql.startsWith("show ") || sql.startsWith("desc ") || sql.startsWith("select ") || sql.startsWith("SELECT ")|| sql.startsWith("\nselect ")){
                        // 4---查询操作
                        ps=conn.prepareStatement(sql);
                        //执行sql语句   返回的是一个结果集
                        boolean flag = ps.execute();
                        if(flag){
                        	long count=0;
                            rs=ps.getResultSet();
                            if(sql.startsWith("desc ")){
                            	while(rs.next()) {
                            		sblData.append(rs.getString("Field")).append("    ");
                            		sblData.append(rs.getString("Type")).append("    ");
                            		sblData.append(rs.getString("Null")).append("    ");
                            		sblData.append(rs.getString("Key")).append("    ");
                            		sblData.append(rs.getString("Default")).append("    ");
	                            	sblData.append(rs.getString("Extra")).append("    ").append("\n");
	                            	count++;
                            	}
                            }else if(sql.startsWith("show ")){
                            	sblData.append("Name").append("    ").append("Value").append("\n");
                            	while(rs.next()) {
                            		sblData.append(rs.getString(1)).append("    ").append(rs.getString(2)).append("\n");
	                            	count++;
                            	}
                            }else {
                                ResultSetMetaData rsmd=ps.getMetaData();
                                long colNum = rsmd.getColumnCount();
                                //组织所有列头
                                for(int i=1;i<=colNum;i++){
                                    String colName= rsmd.getColumnName(i);
                                    headMap.put(String.valueOf(i),colName);
                                    sblData.append(colName).append("    ");
                                }
                                sblData.append("\n").append("================================================").append("\n");
                                //通过列头获取所有数据。
                              
                                while (rs.next()){
                                	for (int j=1;j<=colNum;j++){
                                		String value = rs.getString(j)==null?"-":rs.getString(j);
                                		String name=headMap.get(String.valueOf(j));
                                		sblData.append(value).append("   ");
//                                    sblData.append(name).append(":").append(value);
                                	}
                                	sblData.append("\n").append("================================================").append("\n");
                                	count++;
                                }
                            }
                            sblData.append("\n").append("sel "+count+" data.").append("\n");
                        }else{
                            sblData.append("\n").append("sel.execute.false").append("\n");
                        }
                    }else if(sql.startsWith("CREATE ") ||sql.startsWith("\nCREATE ") || sql.startsWith("ALTER ")|| sql.startsWith("\nALTER ")){
                    	ps=conn.prepareStatement(sql);
                    	int num =ps.executeUpdate();
                        sblData.append("\n").append("DDL....").append(num).append("\n");
                    }else if(sql.startsWith("DROP ") ||sql.startsWith("\nDROP ") || sql.startsWith("drop ")|| sql.startsWith("\ndrop ")){
                        ps=conn.prepareStatement(sql);
                        int num =ps.executeUpdate();
                        sblData.append("\n").append("DDL....").append(num).append("\n");
                    } else {
                        sblData.append("\n").append("DDL....").append("无此sql语句!").append("\n");
                    }
            	} catch (Exception e) {
                  throw e;
                } finally {
                    if (null!=rs){
                        rs.close();
                    } if (null!=ps){
                        ps.close();
                    } 
                }
            }
            	
//            if(sql.startsWith("update ") || sql.startsWith("UPDATE ")){
//                ps=conn.prepareStatement(sql);
//                int  rows=ps.executeUpdate();
//                sblData.append("\n").append("更新"+rows+"条数据.").append("\n");
//
//            }else   if(sql.startsWith("delete ") || sql.startsWith("DELETE ")){
//                // 2--删除操作
//                    ps=conn.prepareStatement(sql);
//                    int  rows=ps.executeUpdate();
//                    sblData.append("\n").append("删除"+rows+"条数据.").append("\n");
//                    // 3---新增操作
//                }else   if(sql.startsWith("insert ") || sql.startsWith("INSERT ")){
//                ps=conn.prepareStatement(sql);
//                int  rows=ps.executeUpdate();
//                sblData.append("\n").append("新增"+rows+"条数据.").append("\n");
//            }else  if(sql.startsWith("select ") || sql.startsWith("SELECT ")){
//                // 4---查询操作
//                ps=conn.prepareStatement(sql);
//                //执行sql语句   返回的是一个结果集
//                boolean flag = ps.execute();
//                if(flag){
//                    rs=ps.getResultSet();
//                    ResultSetMetaData rsmd=ps.getMetaData();
//                    long colNum = rsmd.getColumnCount();
//                    //组织所有列头
//                    for(int i=1;i<=colNum;i++){
//                        String colName= rsmd.getColumnName(i);
//                        headMap.put(String.valueOf(i),colName);
//                        sblData.append(colName).append("    ");
//                    }
//                    sblData.append("\n").append("================================================").append("\n");
//                    //通过列头获取所有数据。
//                    while (rs.next()){
//                        for (int j=1;j<=colNum;j++){
//                            String value = rs.getString(j)==null?"-":rs.getString(j);
//                            String name=headMap.get(String.valueOf(j));
//                            sblData.append(value).append("   ");
////                            sblData.append(name).append(":").append(value);
//                        }
//                        sblData.append("\n").append("================================================").append("\n");
//                    }
//                }else{
//                    sblData.append("\n").append("更新成功").append("\n");
//                }
//            }else {
//            	ps=conn.prepareStatement(sql);
//            	ps.executeUpdate();
//                sblData.append("\n").append("DDL语句.").append("\n");
//            }
            sblData.append("END......").append("\n");
            map.put("sql",sblData.toString());
            conn.commit();

            /*while (rs.next()) {//循环遍历
                user=new User();
                user.setName(rs.getString("name"));
                user.setSex(rs.getString("sex"));
            }
            //输出
            System.out.println(user.getName());*/

            //关闭
           // conn.rollback();

      
        }catch (Exception e) {
            e.printStackTrace();
            map.put("sql",e.getMessage());
        } finally {
            if (null!=rs){
                rs.close();
            } if (null!=ps){
                ps.close();
            } if (null!=conn){
                conn.close();
            }
        }

        //map.put("data","====================================");
        return map;
    }

    /*private static void testFn1() {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String strUrl = "jdbc:oracle:thin:@20.200.89.10:1521:idealdbs";
        String username = "ry";
        String password = "ry";
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        CallableStatement proc = null;
        try {
            Class.forName(driver);
            conn = DriverManager
                    .getConnection(strUrl, "junittest", "junittest");
            // 简单无返回值的存储过程调用
            // proc = conn.prepareCall("{ call addpfuser(?,?) }");
            // proc.setString(1, "testid");
            // proc.setString(2, "从程序中插入的数据");
            // 单个返回值的存储过程调用
            proc = conn.prepareCall("{ call getUserNameById(?,?) }");
            proc.setString(1, "testi");
            proc.registerOutParameter(2, Types.VARCHAR);
            proc.execute();
            String testPrint = proc.getString(2);
            System.out.println(testPrint);
            // 多列返回值的存储过程调用
            // proc = conn.prepareCall("{ call testc(?) }");
            // proc.registerOutParameter(1, OracleTypes.CURSOR);
            // proc.execute();
            // rs = (ResultSet) proc.getObject(1);
            // while (rs.next()) {
            // System.out.println(rs.getString(1) + "," + rs.getString(2));
            // }
            // proc = conn.prepareCall("{ call delZeroCell(?,?,?) }");
            // proc.setString(1, "user_role");
            // proc.setString(2, "xzqdm");
            // proc.setString(3, null);
            // proc.execute();
        } catch (SQLException ex2) {
            ex2.printStackTrace();
        } catch (Exception ex2) {
            ex2.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                }
            } catch (SQLException ex1) {
            }
        }
    }*/

    /*public int add(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2.获得数据库链接
            conn = DriverManager.getConnection(url, username, password);
            sql = "insert into t_user(name,age)values(?,?) ";
            //3.通过数据库的连接操作数据库，实现增删改查
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                ps.close();
                conn.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return 0;
    }*/

    /**
     * 第五步：开发增删改查的方法
     * 1、执行增删改SQL语句的方法
     * 2、执行查询SQL语句的方法
     * 3、批量执行SQL语句的方法
     */

    /**
     * 执行增删改SQL语句
     * @param sql
     * @param params
     * @return 影响的行数
     */
        int rtn = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        public int executeUpdate(String sql, Object[] params) {


            try {
            //conn = getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);

            if(params != null && params.length > 0) {
                for(int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            rtn = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            if(conn != null) {
                datasource.push(conn);
            }
        }*/

        return rtn;
    }

    /**
     * 执行查询SQL语句
     * @param sql
     * @param params
     * @param callback
     */
    public void executeQuery(String sql, Object[] params,
                             QueryCallback callback) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            if(params != null && params.length > 0) {
                for(int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            rs = pstmt.executeQuery();

            callback.process(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            if(conn != null) {
                datasource.push(conn);
            }
        }*/
    }

    /**
     * 批量执行SQL语句
     *
     * 批量执行SQL语句，是JDBC中的一个高级功能
     * 默认情况下，每次执行一条SQL语句，就会通过网络连接，向MySQL发送一次请求
     *
     * 但是，如果在短时间内要执行多条结构完全一模一样的SQL，只是参数不同
     * 虽然使用PreparedStatement这种方式，可以只编译一次SQL，提高性能，但是，还是对于每次SQL
     * 都要向MySQL发送一次网络请求
     *
     * 可以通过批量执行SQL语句的功能优化这个性能
     * 一次性通过PreparedStatement发送多条SQL语句，比如100条、1000条，甚至上万条
     * 执行的时候，也仅仅编译一次就可以
     * 这种批量执行SQL语句的方式，可以大大提升性能
     *
     * @param sql
     * @param paramsList
     * @return 每条SQL语句影响的行数
     */
    public int[] executeBatch(String sql, List<Object[]> paramsList) {
        int[] rtn = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            //conn = getConnection();

            // 第一步：使用Connection对象，取消自动提交
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);

            // 第二步：使用PreparedStatement.addBatch()方法加入批量的SQL参数
            if(paramsList != null && paramsList.size() > 0) {
                for(Object[] params : paramsList) {
                    for(int i = 0; i < params.length; i++) {
                        pstmt.setObject(i + 1, params[i]);
                    }
                    pstmt.addBatch();
                }
            }

            // 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
            rtn = pstmt.executeBatch();

            // 最后一步：使用Connection对象，提交批量的SQL语句
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            if(conn != null) {
                datasource.push(conn);
            }
        }*/

        return rtn;
    }

    /**
     * 静态内部类：查询回调接口
     *
     *
     */
    public interface QueryCallback {

        /**
         * 处理查询结果
         * @param rs
         * @throws Exception
         */
        void process(ResultSet rs) throws Exception;

    }

}
