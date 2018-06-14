package acessoria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
public class DatabaseAcess {
    private Connection connection;
    public DatabaseAcess(String user, String pass){
        try{

            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@grad.icmc.usp.br:15215:orcl",
                    user,
                    pass);
        }catch(Exception e){
            System.out.println("Não consegui conectar ao banco de dados");
            System.out.println(e);
        }
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

    public int createCliente(){
        Statement st;
        try{
            st = connection.createStatement();
            return st.executeUpdate("INSERT INTO TIME(NOME, ESTADO, TIPO, SALDO_GOLS) VALUES (\'ASDAD\', \'SE\', \'AMADOR\', 0)");
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return 0;
    }

}
