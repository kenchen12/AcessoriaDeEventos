package acessoria;

import java.sql.Connection;
import java.sql.DriverManager;

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

}
