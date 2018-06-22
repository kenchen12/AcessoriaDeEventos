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

        try {
            st2 = db.getConnection().createStatement();
            rs2 = st2.executeQuery("SELECT NOTA_FISCAL, TIPO FROM FESTA WHERE NOTA_FISCAL='"+check+"'");
        }
        catch(Exception e) {}

        switch(tableName) {
        case "CONTRATO_ANIMADOR":
        case "CONTRATO_BRINQUEDO":
            try {
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
        case "CONTRATO_SOM_LUZ":
        case "CONTRATO_CERIMONIALISTA":
        case "CONTRATO_BANDA":
            try {
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
        default:
            return true;
        }
    }

    public static boolean tipoLocal(DatabaseAccess db, String tableName, ArrayList<String> input) {
        Statement st1 = null, st2 = null;
        ResultSet rs1 = null, rs2 = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0, n = -1;
        
        try {
            st1 = db.getConnection().createStatement();
            rs1 = st1.executeQuery("SELECT * FROM " + tableName);            
            rsmd = rs1.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        try {            
            n = rs1.findColumn("LOCAL");
        }
        catch (Exception e) {}

        if(n == -1)
            return true;
        
        String check = input.get(n-1);

        try {
            st2 = db.getConnection().createStatement();
            rs2 = st2.executeQuery("SELECT ID, TIPO FROM LOCAL WHERE ID='"+check+"'");
        }
        catch(Exception e) {}

        switch(tableName) {
        case "BUFE_INFANTIL":
            try {
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
        case "DECORACAO_SALAO":
            try {
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
        default:
            return true;
        }
    }

    public static boolean festaLocal(DatabaseAccess db, String tableName, ArrayList<String> input) {
        Statement st1 = null, st2 = null;
        ResultSet rs1 = null, rs2 = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0, n = -1, m = -1;
        
        try {
            st1 = db.getConnection().createStatement();
            rs1 = st1.executeQuery("SELECT * FROM " + tableName);            
            rsmd = rs1.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        try {            
            n = rs1.findColumn("LOCAL");
        }
        catch (Exception e) {}

        if(n == -1)
            return true;
        
        String checkLocal = input.get(n-1);

        try {            
            m = rs1.findColumn("TIPO");
        }
        catch (Exception e) {}

        if(m == -1)
            return true;
        
        String checkFesta = input.get(m-1);

        try {
            st2 = db.getConnection().createStatement();
            rs2 = st2.executeQuery("SELECT L.TIPO AS TIPO_LOCAL FROM FESTA F JOIN LOCAL L ON L.ID="+checkLocal);
        }
        catch(Exception e) {}

        switch(tableName) {
        case "FESTA":
            try {
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
        default:
            return true;
        }
    }
}
