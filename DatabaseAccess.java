package assessoria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

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

    public int createCliente(String cpf, String nome, String email, String telefone){
        Statement st;
        if(email.length() == 0){
            email = "NULL";
        }
        if(telefone.length() == 0){
            telefone = "NULL";
        }

        try{
            String sql = "INSERT INTO CLIENTE VALUES('"+cpf+"', '" + nome + "', '" + email + "', '" + telefone +"')";
            st = connection.createStatement();
            return st.executeUpdate(sql);
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return 0;
    }
    
    public int createConvidado(String festa, String nome){
        Statement st;

        try{
            String sql = "INSERT INTO CONVIDADO VALUES('"+festa+"', '" +nome+ "')";
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
