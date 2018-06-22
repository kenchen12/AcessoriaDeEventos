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
    
    public int insertColumn(String tableName, ArrayList<String> value) {
        Statement st;

        try{
            /* Loop through array list to create insert query */
            String sql = "INSERT INTO "+tableName+" VALUES(";
            for(int i = 0; i < value.size(); i++) {
                if(i == value.size()-1) {
                    sql+= "'"+value.get(i)+"')";
                }
                else {
                    sql += "'"+value.get(i)+"',";
                }
            }
            /* Execute query */
            st = connection.createStatement();
            return st.executeUpdate(sql);
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return 0;
    }
    
    public int updateColumn(String tableName, String cpf, String novo, String coluna) {
        Statement st;

        try{
            String sql = "UPDATE "+tableName+" SET " + coluna + "= '" + novo + "' WHERE " + coluna + " = '" + cpf + "'";
            st = connection.createStatement();
            return st.executeUpdate(sql);
        }
        catch(Exception e){
			//e.printStackTrace();
        }
        return 0;
    }

    
    public Connection getConnection() {
        return this.connection;
    }

}
