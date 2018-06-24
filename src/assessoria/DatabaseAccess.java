package assessoria;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;
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
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            /* Loop through array list to create insert query */
            String sql = "INSERT INTO "+tableName+" VALUES(";
            for(int i = 0; i < value.size(); i++) {
                String input = null;
                if(rsmd.getColumnTypeName(i+1).equals("DATE")) {
                    input = "TO_DATE('"+value.get(i)+"', 'DD/MM/YYYY HH24:MI')";
                }
                else {
                    input = value.get(i);
                }
                if(i == value.size()-1) {
                    if(input.equals(""))
                        sql+= "NULL)";
                    else if(rsmd.getColumnTypeName(i+1).equals("DATE"))
                        sql+= input+")";
                    else
                        sql+= "'"+input+"')";
                }
                else {
                    if(input.equals(""))
                        sql+= "NULL,";
                    else if(rsmd.getColumnTypeName(i+1).equals("DATE"))
                        sql+= input+",";
                    else
                        sql+= "'"+input+"',";
                }
            }
            /* Execute query */
            st = connection.createStatement();
            sql = Utils.deAccent(sql);
            return st.executeUpdate(sql);
        }
        catch(SQLException e){
			int code = e.getErrorCode();
            switch(code) {
            case 1:
                System.out.println("Valor já existente na tabela");
                break;
            case 1843:
            case 1847:
            case 1850:
                System.out.println("Formato de data/hora inválido");
                break;
            case 2291:
                System.out.println("Referência externa não encontrada");
                break;
            case 2292:
                System.out.println("Valor a ser removido é referenciado externamente");
                    break;
            }
        }
        return 0;
    }
  
    public int updateColumn(String tableName, String coluna, String antigo, String novo, ArrayList<String> pk) {
        Statement st;

        try{
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet primaryKey = meta.getPrimaryKeys(null, null, tableName);
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int nCols = rsmd.getColumnCount(), i;
            for(i = 1; i <= nCols; i++)
                if(rsmd.getColumnName(i).equals(coluna) && rsmd.getColumnTypeName(i).equals("DATE"))
                    break;
            String input = null;
            String sql = "UPDATE "+tableName+" SET " + coluna + "=";
            if(novo.equals(""))
                sql += "NULL";
            else if(i <= nCols && i >= 1)
                sql += "TO_DATE('"+novo+"', 'DD/MM/YYYY HH24:MI')";
            else
                sql += "'"+novo+"'";
            sql += " WHERE "+coluna+"=";
            if(antigo.equals(""))
                sql += "NULL";
            else if(i <= nCols && i >= 1)
                sql += "TO_DATE('"+antigo+"', 'DD/MM/YYYY HH24:MI')";
            else
                sql += "'"+antigo+"'";
            for(String str : pk) {
                sql += " AND ";
                int k = 0;
                primaryKey.next();
                while(!primaryKey.isAfterLast()) {
                    sql += primaryKey.getString("COLUMN_NAME") + "=";
                    if(pk.get(k).equals(""))
                        sql += "NULL";
                    else if(i <= nCols && i >= 1)
                        sql += "TO_DATE('"+pk.get(k)+"', 'DD/MM/YYYY HH24:MI')";
                    else
                        sql += "'"+pk.get(k)+"'";
                    k++;
                    primaryKey.next();
                }
            }
            st = connection.createStatement();
            sql = Utils.deAccent(sql);
            return st.executeUpdate(sql);
        }
        catch(SQLException e){
			int code = e.getErrorCode();
            switch(code) {
            case 1:
                System.out.println("Valor já existente na tabela");
                break;
            case 1843:
            case 1847:
            case 1850:
                System.out.println("Formato de data/hora inválido");
                break;
            case 2291:
                System.out.println("Referência externa não encontrada");
                break;
            case 2292:
                System.out.println("Valor a ser removido é referenciado externamente");
                    break;
            default:
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int removeColumn(String tableName, ArrayList<String> pk) {
        Statement st;

        try{
            DatabaseMetaData meta = this.connection.getMetaData();
            ResultSet primaryKey = meta.getPrimaryKeys(null, null, tableName);            
            st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int nCols = rsmd.getColumnCount();

            String sql = "DELETE FROM "+tableName+" WHERE ";
            int k = 0;
            primaryKey.next();
            while(!primaryKey.isAfterLast()) {
                sql += primaryKey.getString("COLUMN_NAME") + "=";
                if(pk.get(k).equals(""))
                    sql += "NULL";
                else
                    sql += "'"+pk.get(k)+"'";
                k++;
                primaryKey.next();
                if(!primaryKey.isAfterLast())
                    sql += " AND ";
            }
            st = connection.createStatement();
            sql = Utils.deAccent(sql);
            return st.executeUpdate(sql);
        }
        catch(SQLException e){
			int code = e.getErrorCode();
            switch(code) {
            case 1:
                System.out.println("Valor já existente na tabela");
                break;
            case 1843:
            case 1847:
            case 1850:
                System.out.println("Formato de data/hora inválido");
                break;
            case 2291:
                System.out.println("Referência externa não encontrada");
                break;
            case 2292:
                System.out.println("Valor a ser removido é referenciado externamente");
                    break;
            default:
                e.printStackTrace();
            }
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
            return st.executeQuery("SELECT ANIMADOR.CPF, ANIMADOR.NOME_ARTISTICO FROM FESTA JOIN CONTRATO_ANIMADOR ON (FESTA.NOTA_FISCAL = CONTRATO_ANIMADOR.FESTA) AND (EXTRACT(YEAR FROM FESTA.DATA_HORA) = 2018) JOIN PROFISSAO ON (PROFISSAO.ANIMADOR = CONTRATO_ANIMADOR.ANIMADOR) JOIN ANIMADOR ON (ANIMADOR.CPF = PROFISSAO.ANIMADOR) GROUP BY ANIMADOR.CPF, ANIMADOR.NOME_ARTISTICO HAVING (COUNT(ANIMADOR.CPF)) >= 2");
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
