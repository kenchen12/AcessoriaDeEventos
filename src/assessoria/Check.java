package assessoria;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import assessoria.DatabaseAccess;

public class Check {

    public static boolean contratoFesta(DatabaseAccess db, String tableName, ArrayList<String> input) {
        Statement st1 = null, st2 = null;
        ResultSet rs1 = null, rs2 = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0, n = -1;

        /* Get table info */
        try {
            st1 = db.getConnection().createStatement();
            rs1 = st1.executeQuery("SELECT * FROM " + tableName);
            rsmd = rs1.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        /* Find column named 'FESTA' */
        try {
            n = rs1.findColumn("FESTA");
        }
        catch (Exception e) {}

        /* If there are none, there is no reason to continue the check */
        if(n == -1)
            return true;

        String check = input.get(n-1);

        /* Search table 'FESTA' looking for primary key entered */
        try {
            st2 = db.getConnection().createStatement();
            rs2 = st2.executeQuery("SELECT NOTA_FISCAL, TIPO FROM FESTA WHERE NOTA_FISCAL='"+check+"'");
        }
        catch(Exception e) {}

        switch(tableName) {
            /* case 'FESTA INFANTIL' */
        case "CONTRATO_ANIMADOR":
        case "CONTRATO_BRINQUEDO":
            try {
                /* Check if constraints are right */
                rs2.next();
                if(rs2.getString("NOTA_FISCAL").equals(check) &&
                   rs2.getString("TIPO").equals("INFANTIL"))
                    return true;
                if(!rs2.next())
                    return false;
                else {
                    while(!rs2.isAfterLast()) {
                        if(rs2.getString("NOTA_FISCAL").equals(check) &&
                           rs2.getString("TIPO").equals("INFANTIL"))
                            return true;
                        rs2.next();
                    }
                    return false;
                }
            }
            catch(Exception e) {}
        /* CASE 'CASAMENTO' */
        case "CONTRATO_SOM_LUZ":
        case "CONTRATO_CERIMONIALISTA":
        case "CONTRATO_BANDA":
            try {
                /* Check if constraints are right */
                rs2.next();
                if(rs2.getString("NOTA_FISCAL").equals(check) &&
                   rs2.getString("TIPO").equals("CASAMENTO"))
                    return true;
                if(!rs2.next())
                    return false;
                else {
                    while(!rs2.isAfterLast()) {
                        if(rs2.getString("NOTA_FISCAL").equals(check) &&
                           rs2.getString("TIPO").equals("CASAMENTO"))
                            return true;
                        rs2.next();
                    }
                }
                return false;
            }
            catch (Exception e) {}
        /* case other type of table, do not check */
        default:
            return true;
        }
    }

    public static boolean tipoLocal(DatabaseAccess db, String tableName, ArrayList<String> input) {
        Statement st1 = null, st2 = null;
        ResultSet rs1 = null, rs2 = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0, n = -1;

        /* Get table info */
        try {
            st1 = db.getConnection().createStatement();
            rs1 = st1.executeQuery("SELECT * FROM " + tableName);
            /* Get meta data and number of columns */
            rsmd = rs1.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        /* Try to find a column named 'LOCAL' */
        try {
            n = rs1.findColumn("LOCAL");
        }
        catch (Exception e) {}

        /* If there are none, there is no reason to continue the check */
        if(n == -1)
            return true;
        /* Get 'LOCAL' input from array */
        String check = input.get(n-1);

        try {
            st2 = db.getConnection().createStatement();
            rs2 = st2.executeQuery("SELECT ID, TIPO FROM LOCAL WHERE ID='"+check+"'");
        }
        catch(Exception e) {}

        switch(tableName) {
            /* If insert is into 'BUFE_INFANTIL' */
        case "BUFE_INFANTIL":
            try {
                /* Check if constraints are right */
                rs2.next();
                if(rs2.getString("ID").equals(check) &&
                   rs2.getString("TIPO").equals("BUFE INFANTIL"))
                    return true;
                if(!rs2.next())
                    return false;
                else {
                    while(!rs2.isAfterLast()) {
                        if(rs2.getString("ID").equals(check) &&
                           rs2.getString("TIPO").equals("BUFE INFANTIL"))
                            return true;
                        rs2.next();
                    }
                    return false;
                }
            }
            catch(Exception e) {}
            /* If insert is into 'DECORACAO_SALAO' */
        case "DECORACAO_SALAO":
            try {
                /* Check if constraints are right */
                rs2.next();
                if(rs2.getString("ID").equals(check) &&
                   rs2.getString("TIPO").equals("BUFE INFANTIL"))
                    return true;
                if(!rs2.next())
                    return false;
                else {
                    while(!rs2.isAfterLast()) {
                        if(rs2.getString("ID").equals(check) &&
                           rs2.getString("TIPO").equals("SALAO"))
                            return true;
                        rs2.next();
                    }
                }
                return false;
            }
            catch (Exception e) {}
            /* Do not check if table is not one of the above */
        default:
            return true;
        }
    }

    public static boolean festaLocal(DatabaseAccess db, String tableName, ArrayList<String> input) {
        Statement st1 = null, st2 = null;
        ResultSet rs1 = null, rs2 = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0, n = -1, m = -1;

        /* Get table meta data */
        try {
            st1 = db.getConnection().createStatement();
            rs1 = st1.executeQuery("SELECT * FROM " + tableName);
            rsmd = rs1.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        /* Try to find column named 'LOCAL' */
        try {
            n = rs1.findColumn("LOCAL");
        }
        catch (Exception e) {}

        if(n == -1)
            return true;

        String checkLocal = input.get(n-1);

        /* Try to find column named 'TIPO' */
        try {
            m = rs1.findColumn("TIPO");
        }
        catch (Exception e) {}

        if(m == -1)
            return true;

        String checkFesta = input.get(m-1);

        /* Get constraints info from tables */
        try {
            st2 = db.getConnection().createStatement();
            rs2 = st2.executeQuery("SELECT L.TIPO AS TIPO_LOCAL FROM FESTA F JOIN LOCAL L ON L.ID="+checkLocal);
        }
        catch(Exception e) {}
        switch(tableName) {
        case "FESTA":
            try {
                /* Check constraints */
                rs2.next();
                if(rs2.getString("TIPO_LOCAL").equals("BUFE INFANTIL") &&
                   checkFesta.equals("INFANTIL"))
                    return true;
                else if(rs2.getString("TIPO_LOCAL").equals("SALAO") &&
                        checkFesta.equals("CASAMENTO"))
                    return true;
                if(!rs2.next())
                    return false;
                else {
                    while(!rs2.isAfterLast()) {
                        if(rs2.getString("TIPO_LOCAL").equals("BUFE INFANTIL") &&
                           checkFesta.equals("INFANTIL"))
                            return true;
                        else if(rs2.getString("TIPO_LOCAL").equals("SALAO") &&
                                checkFesta.equals("CASAMENTO"))
                            return true;
                        rs2.next();
                    }
                    return false;
                }
            }
            catch(Exception e) {}
            /* Do not check if table is not 'FESTA' */
        default:
            return true;
        }
    }

    public static boolean updateContratoFesta(DatabaseAccess db, String tableName, String columnName, String newValue) {
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int nCols = -1;

        if(!columnName.equals("FESTA"))
            return true;
        
        try {
            st = db.getConnection().createStatement();
            rs = st.executeQuery("SELECT TIPO FROM FESTA WHERE NOTA_FISCAL='"+newValue+"'");
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();   
        }
        catch (Exception e) {}

        try {
            rs.next();
            switch(tableName) {
            case "CONTRATO_ANIMADOR":
            case "CONTRATO_BRINQUEDO":
                if(rs.getString("TIPO").equals("CASAMENTO"))
                    return false;
                else
                    return true;
            case "CONTRATO_SOM_LUZ":
            case "CONTRATO_CERIMONIALISTA":
            case "CONTRATO_BANDA":
                if(rs.getString("TIPO").equals("INFANTIL"))
                    return false;
                else
                    return true;
            default:
                return true;
            }
        }
        catch (Exception e) {}
        return true;
    }

    public static boolean updateTipoLocal(DatabaseAccess db, String tableName, String columnName, String newValue) {
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int nCols = -1;

        if(!columnName.equals("LOCAL"))
            return true;
        
        try {
            st = db.getConnection().createStatement();
            rs = st.executeQuery("SELECT TIPO FROM LOCAL WHERE ID='"+newValue+"'");
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();   
        }
        catch (Exception e) {}

        try {
            rs.next();
            switch(tableName) {
            case "BUFE_INFANTIL":
                if(rs.getString("TIPO").equals("SALAO"))
                    return false;
                else
                    return true;
            case "DECORACAO_SALAO":
                if(rs.getString("TIPO").equals("BUFE INFANTIL"))
                    return false;
                else
                    return true;
            default:
                return true;
            }
        }
        catch (Exception e) {}
        return true;   
    }

    public static boolean updateFestaLocal(DatabaseAccess db, String tableName, String columnName, String oldValue, String newValue, String oldPK) {
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int nCols = -1;

        if(!columnName.equals("LOCAL") && !columnName.equals("TIPO"))
            return true;
        
        try {
            st = db.getConnection().createStatement();
            rs = st.executeQuery("SELECT L.TIPO AS TIPO_LOCAL, F.TIPO AS TIPO_FESTA FROM LOCAL L JOIN FESTA F ON L.ID='"+newValue+"' AND F.NOTA_FISCAL='"+oldPK+"'");
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();   
        }
        catch (Exception e) {}

        try {
            rs.next();
            switch(tableName) {
            case "FESTA":
                if(rs.getString("TIPO_FESTA").equals("INFANTIL") &&
                   rs.getString("TIPO_LOCAL").equals("SALAO"))
                    return false;
                if(rs.getString("TIPO_FESTA").equals("CASAMENTO") &&
                   rs.getString("TIPO_LOCAL").equals("BUFE INFANTIL"))
                    return false;
                else
                    return true;
            }
        }
        catch (Exception e) {}
        return true;   
    }
}
