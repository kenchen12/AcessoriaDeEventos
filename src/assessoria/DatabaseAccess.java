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
			e.printStackTrace();
        }
        return 0;
    }

    public int removeColumn(String tableName, String valor, String coluna) {
        Statement st;

        try{
            String sql = "DELETE FROM "+tableName+" WHERE " + coluna + "= '" + valor + "'";
            st = connection.createStatement();
            return st.executeUpdate(sql);
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return 0;
    }

    public ResultSet select1() {
        Statement st;

        try{
            st = connection.createStatement();
            return st.executeQuery("SELECT CLIENTE.NOME, CONVIDADO.NOME FROM FESTA JOIN CLIENTE ON (FESTA.CLIENTE = CLIENTE.CPF) AND ( EXTRACT(YEAR FROM FESTA.DATA_HORA) >= 2010) AND ( EXTRACT(YEAR FROM FESTA.DATA_HORA) <= 2018) JOIN CONVIDADO ON (CONVIDADO.FESTA = FESTA.NOTA_FISCAL) ORDER BY CLIENTE.NOME");
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return null;
    }

    public ResultSet select2() {
        Statement st;

        try{
            st = connection.createStatement();
            return st.executeQuery("SELECT EQUIPE_BUFE.NOME, COUNT(EQUIPE_BUFE.CNPJ) AS QUANTIDADE FROM FESTA JOIN EQUIPE_BUFE ON (FESTA.EQUIPE_BUFE = EQUIPE_BUFE.CNPJ) AND (FESTA.TIPO = 'CASAMENTO') GROUP BY EQUIPE_BUFE.CNPJ, EQUIPE_BUFE.NOME");
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return null;
    }

    public ResultSet select3() {
        Statement st;

        try{
            st = connection.createStatement();
            return st.executeQuery("SELECT ANIMADOR.CPF, ANIMADOR.NOME_ARTISTICO FROM FESTA JOIN CONTRATO_ANIMADOR ON (FESTA.NOTA_FISCAL = CONTRATO_ANIMADOR.FESTA) AND (EXTRACT(YEAR FROM FESTA.DATA_HORA) = 2018) JOIN PROFISSAO ON (PROFISSAO.ANIMADOR = CONTRATO_ANIMADOR.ANIMADOR) JOIN ANIMADOR ON (ANIMADOR.CPF = PROFISSAO.ANIMADOR) GROUP BY ANIMADOR.CPF, ANIMADOR.NOME_ARTISTICO HAVING (COUNT(ANIMADOR.CPF) >= 2");
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return null;
    }

    public ResultSet select4() {
        Statement st;

        try{
            st = connection.createStatement();
            return st.executeQuery("SELECT FESTA.NOTA_FISCAL, BANDA.NOME, BANDA.GENERO FROM CONTRATO_BANDA JOIN BANDA ON (CONTRATO_BANDA.CPF = BANDA.CPF) AND (CONTRATO_BANDA.NOME = BANDA.NOME) RIGHT JOIN FESTA ON (FESTA.NOTA_FISCAL = CONTRATO_BANDA.FESTA)");
        }
        catch(Exception e){
			e.printStackTrace();
        }
        return null;
    }

    public ResultSet createView(String tableName) {
        Statement st;

        try{
            st = connection.createStatement();
            return st.executeQuery("SELECT * FROM "+tableName);
            /* Execute query */
        }
        catch(Exception e){
			//e.printStackTrace();
        }
        return null;
    }

    
    public Connection getConnection() {
        return this.connection;
    }

}
