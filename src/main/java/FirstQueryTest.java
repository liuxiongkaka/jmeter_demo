import com.google.gson.JsonObject;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class FirstQueryTest implements JavaSamplerClient {

    Connection conn ;
    Statement ps;

    String table_name = "app_user";
    String sql = " SELECT * FROM " + table_name + " WHERE lower_user_name = ";
    String pj = "''".substring(1,2);

    ResultSet result_set;
    ArrayList<String> results = new ArrayList<>();

    @Override
    public void setupTest(JavaSamplerContext javaSamplerContext) {

    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel("FirstQuery-Mysql-Test");
        sampleResult.sampleStart();

        String url = javaSamplerContext.getParameter("url");
        String user_name = javaSamplerContext.getParameter("user_name");
        String pass_wd = javaSamplerContext.getParameter("pass_wd");
        String sql_name = javaSamplerContext.getParameter("sql_name");
        sql = sql + pj + sql_name + pj + " ";

        JsonObject response_js = new JsonObject();

        System.out.println(sql);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user_name,pass_wd);
            ps = conn.createStatement();
            result_set = ps.executeQuery(sql);

            while(result_set.next()){

                String result = result_set.getString(2)+":"+result_set.getString(3);
                results.add(result);
            }
            if(results.size()>0)
            {
                response_js.addProperty("is_true",1);
                response_js.addProperty("code","200");
                response_js.addProperty("message",sql_name + " is existed !");

//                System.out.println(response_js.toString());

                sampleResult.setSuccessful(true);
                sampleResult.setResponseCode("200");
                sampleResult.setResponseData(response_js.toString(),"utf-8");
                sampleResult.setResponseMessage(sql_name + " exists !");
            }
            else
            {
                sampleResult.setSuccessful(false);
                sampleResult.setResponseCode("500");
                sampleResult.setResponseMessage("query failed,sql is : "+sql_name);

            }
        } catch (Exception e) {
            e.printStackTrace();
            sampleResult.setSuccessful(false);
            sampleResult.setResponseCode("500");
            sampleResult.setResponseMessage("query failed,sql is : " + e.getMessage());
        }

        System.out.println(sampleResult.getResponseMessage());

        sampleResult.sampleEnd();

        return  sampleResult;


    }

    @Override
    public void teardownTest(JavaSamplerContext javaSamplerContext) {

        try {
            ps.close(); //关闭对象
            conn.close(); //关闭对象
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument("url","jdbc:mysql://10.55.105.177:3306/jira");
        arguments.addArgument("user_name","root");
        arguments.addArgument("pass_wd","123456");
        arguments.addArgument("sql_name", "admin");
        return arguments;
    }


    public static void main(String[] args) {
        FirstQueryTest F1 = new FirstQueryTest();
        Arguments arguments = F1.getDefaultParameters();
//        System.out.println(arguments);
        JavaSamplerContext context = new JavaSamplerContext(arguments);
        F1.runTest(context);


    }
}
