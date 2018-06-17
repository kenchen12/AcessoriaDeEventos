package assessoria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseAccess {
    
    private Connection connection;
    
    public DatabaseAccess(String user, String pass){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl",
                    user, pass);
        }catch(Exception e){}
    }

    public ResultSet selectCliente(){
        Statement st;
        try{
            st = connection.createStatement();
            return st.executeQuery("SELECT * FROM TIME");
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return null;
    }
    
    public int insertColumn(String tableName, ArrayList<String> value) {
        Statement st;

        try{
            String sql = "INSERT INTO "+tableName+" VALUES(";
            for(int i = 0; i < value.size(); i++) {
                if(i == value.size()-1) {
                    sql+= "'"+value.get(i)+"')";
                }
                else {
                    sql += "'"+value.get(i)+"',";
                }
            }
            st = connection.createStatement();
            return st.executeUpdate(sql);
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return 0;
    }

    
    public Connection getConnection() {
        return this.connection;
    }

}
