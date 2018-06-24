package assessoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import assessoria.DatabaseAccess;

public class UI {

    DatabaseAccess db;
    ArrayList<String> tableName;

    /**
     * The constructor creates a connection with the database
     * and loads all tables from that DB
     */
    public UI() {
        this.db = this.Connect();
        this.tableName = this.setTableNames();
    }

    private DatabaseAccess Connect() {
        DatabaseAccess db;
        Screen.clear();
        while(true) {
            db = null;
            Scanner s = new Scanner(System.in);
            String user, pass;
            System.out.println("Conecte-se ao banco de dados");
            System.out.println("(Digite um usuário vazio para sair)");
            System.out.print("Digite o nome de usuário: ");
            user = s.nextLine();
            /* if string is empty, return to kill program */
            if(user == null || user.isEmpty())
                break;
            System.out.print("Digite a senha: ");
            pass = s.nextLine();
            /* Create db connection */
            db = new DatabaseAccess(user, pass);
            if(db.getConnection() != null) {
                Screen.clear();
                System.out.println("Conexão efetuada com sucesso\n");
                break;
            }
            /* if db == null ERROR */
            else {
                Screen.clear();
                System.out.println("Não foi possível conectar ao banco de dados\n");
            }
        }
        return db;
    }

    private ArrayList<String> setTableNames() {
        Statement st;
        ResultSet rs = null;
        ArrayList<String> tn = new ArrayList<String>();
        try {
            /* Get every table name from db */
            st = db.getConnection().createStatement();
            rs = st.executeQuery("SELECT TABLE_NAME FROM USER_TABLES");
        }
        catch (Exception e) {}
        try {
            /* Store table names on array list */
            while(!rs.isAfterLast()) {
                rs.next();
                tn.add(rs.getString("TABLE_NAME"));
            }
        }
        catch(Exception e) {}
        return tn;
    }

    private String filterMainMenu(String raw) {
        raw = raw.toLowerCase();

        /* return what the user types */
        if(raw.equals("1") || raw.equals("inserir"))
            return "inserir";
        else if(raw.equals("2") || raw.equals("atualizar"))
            return "atualizar";
        else if(raw.equals("3") || raw.equals("visualizar"))
            return "visualizar";
        else if(raw.equals("4") || raw.equals("remover"))
            return "remover";
        else if(raw.equals("5") || raw.equals("sair"))
            return "sair";
        else
            return "";
    }

    /**
     * Method to show the main menu of the application,
     * based on the Database that connected
     */
    public void mainMenu() {
        /* if could not connect to db,
           do not show main menu */
        if(this.db == null)
            return;
        while(true) {
            System.out.println("O que deseja fazer?");
            System.out.println("1. Inserir");
            System.out.println("2. Atualizar");
            System.out.println("3. Visualizar");
            System.out.println("4. Remover");
            System.out.println("5. Sair");

            /* Get input and filter it */
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = this.filterMainMenu(input);

            Screen.clear();

            if(input.equals("inserir")) {
                this.insert();
            }
            else if(input.equals("atualizar")) {
				this.update();
            }
            else if(input.equals("visualizar")) {
                this.viewMenu();
            }
            else if(input.equals("remover")) {
                this.remove();
            }
            else if(input.equals("sair")) {
                break;
            }
            else {
                System.out.println("Comando inválido\n");
            }
        }
    }

    private String filterInsert(String raw) {
        raw = raw.toUpperCase();
        int idx = -1;
        /* try to parse string to int to see if user typed a number */
        try {
            idx = Integer.parseInt(raw);
        }
        catch(Exception e) {}

        /* return the 'exit' option */
        if(idx == this.tableName.size()+ 1 || raw.equals("SAIR")) {
            return "SAIR";
        }

        /* return the table name of the index typed */
        if(idx != -1 && idx <= this.tableName.size() && idx > 0)
            return this.tableName.get(idx-1);

        /* return the string typed if it is already the table name */
        if(this.tableName.contains(raw))
            return raw;
        /* return empty string for invalid typing */
        else
            return "";
    }


    private void createInsertInput(String tableName) {
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0;
        Scanner s = new Scanner(System.in);
        String aux = null;
        ArrayList<String> input = new ArrayList<String>();

        /* get every column name and number of columns
           from table chosen */
        try {
            st = this.db.getConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM " + tableName);
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        System.out.println("Digite os dados a serem inseridos");

        while(true) {
            /* loop through every column */
            for(int col = 1; col <= nCols; col++) {
                String name = null;
                try {
                    name = rsmd.getColumnName(col);
                }
                catch (Exception e) {}

                while(true) {
                    /* show column name and get input from user */
                    System.out.print(name + ": ");
                    aux = s.nextLine();
                    int nullable = 0;

                    /* Check if column has 'NOT NULL' */
                    try {
                        nullable = rsmd.isNullable(col);
                    }
                    catch(Exception e) {}

                    /* if user type empty string on 'NOT NULL' field, show error */
                    if(aux.equals("") && nullable == ResultSetMetaData.columnNoNulls) {
                        System.out.println("Valor inválido. Este campo é obrigatório");
                    }
                    else {
                        /* add to array list if input is correct */
                        input.add(aux);
                        break;
                    }
                }
            }

            if(!Check.contratoFesta(this.db, tableName, input)) {
                Screen.clear();
                System.out.println("Tipo de festa incorreta para o contrato\n");
                input.clear();
                continue;
            }
            else if(!Check.tipoLocal(this.db, tableName, input)) {
                Screen.clear();
                System.out.println("Tipo de local incorreto para o valor inserido\n");
                input.clear();
                continue;
            }
            else if(!Check.festaLocal(this.db, tableName, input)) {
                Screen.clear();
                System.out.println("Tipo de local inválido para tipo de festa\n");
                input.clear();
                continue;
            }

            System.out.println("Os dados estão corretos?");
            System.out.println("1. Sim");
            System.out.println("2. Não");

            String answer = s.nextLine();
            answer = answer.toLowerCase();

            if(answer.equals("1") || answer.equals("sim")) {
                Screen.clear();
                input = Utils.deAccentArray(input);
                /* Trying to insert on table */
                int ret = this.db.insertColumn(tableName, input);

                /* Success */
                if(ret != 0) {
                    System.out.println("Inserção efetuada com sucesso");
                    break;
                }
                /* Error on insertion */
                else {
                    System.out.println("Não foi possível inserir, deseja inserir de novo?");
                    while(true) {
                        System.out.println("1. Sim");
                        System.out.println("2. Não");
                        String ans = s.nextLine();
                        ans = ans.toLowerCase();

                        Screen.clear();
                        /* Reinsert data */
                        if(ans.equals("1") || ans.equals("sim")) {
                            System.out.println("Reinsira os dados\n");
                            input.clear();
                            break;
                        }
                        /* Return to menu */
                        else if(ans.equals("2") || ans.equals("não"))
                            return;
                        /* Invalid input */
                        else {
                            Screen.clear();
                            System.out.println("Resposta inválida");
                        }
                    }
                }
            }
            /* if user does not confirm data  */
            else if(answer.equals("2") || answer.equals("não")) {
                Screen.clear();
                System.out.println("O que deseja fazer?");

                /* Ask to quit o reinsert wrong data */
                while(true) {
                    System.out.println("1. Reinserir dados");
                    System.out.println("2. Sair");

                    String ans = s.nextLine();
                    Screen.clear();
                    /* Reinsert */
                    if(ans.equals("1") || ans.equals("reinserir dados") ||
                       ans.equals("reinserir")) {
                        System.out.println("Reinsira os dados\n");
                        input.clear();
                        break;
                    }
                    /* Quit */
                    else if(ans.equals("2") || ans.equals("sair")) {
                        return;
                    }
                    /* Invalid input */
                    else {
                        System.out.println("Resposta inválida");
                    }
                }
            }
        }
    }

    private void insert() {
        while(true) {
            int i = 1;
            System.out.println("Em qual tabela gostaria de inserir?");
            /* Show every table name and exit option */
            for(String str : this.tableName) {
                System.out.println(i + ". " + str);
                i++;
            }
            System.out.println(i + ". Sair");

            /* Getting input and filtering */
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = filterInsert(input);

            Screen.clear();
            /* Exit */
            if(input.equals(Integer.toString(i)) || input.equals("SAIR"))
                break;
            /* Invalid input */
            else if(input.equals("")) {
                System.out.println("Tabela inválida\n");
            }
            /* Get input from user */
            else {
                this.createInsertInput(input);
            }
        }
    }

    private String filterUpdateInput(ResultSetMetaData rsmd, String raw) {
        raw = raw.toUpperCase();
        int nCols = 0;
        int idx = -1;
        /* Try to parse int to see if user input is a number */
        try {
            idx = Integer.parseInt(raw);
        }
        catch(Exception e) {}

        /* Get number of columns */
        try {
            nCols = rsmd.getColumnCount();
        }
        catch(Exception e) {}

        /* Get column name based on valid index*/
        if(idx >= 1 && idx <= nCols) {
            String name = "";
            try {
                name = rsmd.getColumnName(idx);
            }
            catch(Exception e) {}

            return name;
        }
        /* 'Exit' option */
        else if(idx == nCols+1 || raw.equals("SAIR")) {
            return "SAIR";
        }

        /* loop through column names */
        for(int i = 1; i <= nCols; i++) {
            String cName = "";
            try {
                cName = rsmd.getColumnName(i);
            }
            catch (Exception e) {}
            /* Check if input = column name */
            if(cName.equals(raw)) {
                return cName;
            }
        }
        /* Return empty string for invalid input */
        return "";
    }

    private void createUpdateInput(String tableName) {
        Statement st = null;
        ResultSet rs = null, primaryKey = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0, nPK = -1;
        Scanner s = new Scanner(System.in);
        String antigo = null, novo = null, oldPK = null;
        int coll = 0;
        boolean exit = false;
        DatabaseMetaData meta = null;
        
        /* Get every table column and number of columns */
        try {
            meta = this.db.getConnection().getMetaData();
            st = this.db.getConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM " + tableName);
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        while(true) {
            
            try {
                primaryKey = meta.getPrimaryKeys(null, null, tableName);
            }
            catch (Exception e) {}

            // printar tabela
            generalView(tableName, 1);

            ArrayList<String> pk = new ArrayList<String>();
        
            System.out.println("Digite a linha a ser alterada");
            try {
                primaryKey.next();
                while(!primaryKey.isAfterLast()) {
                    System.out.print(primaryKey.getString("COLUMN_NAME")+": ");
                    String aux = s.nextLine();
                    pk.add(aux);
                    if(primaryKey.getString("COLUMN_NAME").equals("FESTA"))
                        oldPK = aux;
                    primaryKey.next();
                }
            }
            catch (Exception e) {}
            
            /* Show column names and exit option */
            System.out.println("Digite a coluna que deseja alterar");
            for(int col = 1; col <= nCols; col++) {
                String name = null;
                try {
                    name = rsmd.getColumnName(col);
                }
                catch (Exception e) {}
                System.out.println(col + ". " + name);
            }
            System.out.println(nCols+1 + ". Sair");

            /* Get and filter input */
            String columnName = s.nextLine();
            columnName = this.filterUpdateInput(rsmd, columnName);

            /* Empty string is invalid option */
            if(columnName.equals("")) {
                Screen.clear();
                System.out.println("Coluna inválida\n");
                continue;
            }
            /* Exit */
            else if(columnName.equals("SAIR")) {
                Screen.clear();
                exit = true;
                break;
            }
            if(exit)
                break;

            /* Prompt to enter old and new values to update */
            while (true){
                System.out.println("Digite o valor de "+columnName+" a ser alterado: ");
                antigo = s.nextLine();

                System.out.println("Digite o novo valor: ");
                novo = s.nextLine();

                /* Check if column is 'NOT NULL' */
                int nullable = 0;
                try {
                    nullable = rsmd.isNullable(coll);
                }
                catch(Exception e) {}

                /* Prompt error on empty field when column is 'NOT NULL' */
                if(novo.equals("") && nullable == ResultSetMetaData.columnNoNulls) {
                    System.out.println("Valor inválido. Este campo é obrigatório");
                }
                /* Both inputs have valid values */
                else {
                    break;
                }
            }

            if(!Check.updateContratoFesta(this.db, tableName, columnName, novo)) {
                Screen.clear();
                System.out.println("Tipo de festa incorreta para o contrato\n");
                continue;
            }
            else if(!Check.updateTipoLocal(this.db, tableName, columnName, novo)) {
                Screen.clear();
                System.out.println("Tipo de local incorreto para o valor inserido\n");
                continue;
            }
            else if(!Check.updateFestaLocal(this.db, tableName, columnName, antigo, novo, oldPK)) {
                Screen.clear();
                System.out.println("Tipo de local inválido para tipo de festa\n");
                continue;
            }
                
            /* Confirmation prompt */
            System.out.println("O novo dado está correto?");
            System.out.println("1. Sim");
            System.out.println("2. Não");

            String answer = s.nextLine();
            answer = answer.toLowerCase();

            Screen.clear();

            if(answer.equals("1") || answer.equals("sim")) {
                antigo = Utils.deAccent(antigo);
                novo = Utils.deAccent(novo);
                pk = Utils.deAccentArray(pk);
                columnName = Utils.deAccent(columnName);
                /* Try to update table */
                int ret = this.db.updateColumn(tableName, columnName, antigo, novo, pk);
                /* Update success */
                if(ret != 0) {
                    System.out.println("Atualização efetuada com sucesso");
                    break;
                }
                /* Error */
                else {
                    System.out.println("Não foi possível inserir, deseja inserir de novo?\n");
                    while(true) {
                        System.out.println("1. Sim");
                        System.out.println("2. Não");
                        String ans = s.nextLine();
                        ans = ans.toLowerCase();
                        if(ans.equals("1") || ans.equals("sim")) {
                            System.out.println("Reinsira os dados\n");
                            pk.clear();
                            break;
                        }
                        else if(ans.equals("2") || ans.equals("não"))
                            return;
                        else
                            System.out.println("Resposta inválida\n");
                    }
                }
            }
            /* If data is incorrect */
            else if(answer.equals("2") || answer.equals("não")) {
                System.out.println("O que deseja fazer?");
                while(true) {
                    System.out.println("1. Reinserir dados");
                    System.out.println("2. Sair");

                    String ans = s.nextLine();
                    Screen.clear();
                    if(ans.equals("1") || ans.equals("reinserir dados") ||
                       ans.equals("reinserir")) {
                        System.out.println("Reinsira os dados\n");
                        pk.clear();
                        break;
                    }
                    else if(ans.equals("2") || ans.equals("sair"))
                        return;
                    else
                        System.out.println("Resposta inválida\n");
                }
            }
        }
    }

    private void update() {
        while(true) {
            int i = 1;
            System.out.println("Qual tabela gostaria de atualizar?");
            /* Show every table name and exit option */
            for(String str : this.tableName) {
                System.out.println(i + ". " + str);
                i++;
            }
            System.out.println(i + ". Sair");

            /* Get and filter input */
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = filterInsert(input);

            Screen.clear();
            /* Check if input is exit, valid or not */
            if(input.equals(Integer.toString(i)) || input.equals("SAIR"))
                break;
            else if(input.equals("")) {
                System.out.println("Tabela inválida\n");
            }
            /* Valid input to update */
            else {
                this.createUpdateInput(input);
            }
        }
    }

    private void generalView(String input, int opt){
        ResultSetMetaData rsmd = null;
        ResultSet ret = null;
        Scanner s = new Scanner(System.in);
        List<List<String>> output = new ArrayList<>();
        List<String> aux = new ArrayList<>();

        try {
            switch(opt) {
            case 1:
                ret = this.db.createView(input);
                break;
            case 2:
                ret = this.db.select1();
                break;
            case 3:
                ret = this.db.select2();
                break;
            case 4:
                ret = this.db.select3();
                break;
            case 5:
                ret = this.db.select4();
                break;
            case 6:
                ret = this.db.select5();
                break;
            case 7:
                ret = this.db.select6();
                break;
            }
        }
        catch (Exception e) {}
        
        try{
            ret.next();
            rsmd = ret.getMetaData();
            int nCols = rsmd.getColumnCount();
            for(int v = 1; v <= nCols; v++){
                aux.add(rsmd.getColumnName(v));
            }

            output.add(aux);
            while (!ret.isAfterLast()){
                aux = new ArrayList<>();
                for(int v = 1; v <= nCols; v++){
                    aux.add(ret.getString(rsmd.getColumnName(v)));
                }
                output.add(aux);
                ret.next();
            }
            System.out.println(Utils.formatOutput(output));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if(ret != null) {
            return;
        }
        /* Error */
        else {
            System.out.println("Não foi possível vizualizar a tabela selecionada");
        }
    }

    private void viewMenu() {
        while(true) {
            System.out.println("O que gostaria de visualizar?");
            System.out.println("(Digite apenas o número)");
            System.out.println("1. Tabelas");
            System.out.println("2. Nome dos clientes e todos seus condidados nas festas entre 2010 e 2018");
            System.out.println("3. Nome de cada equipe e quantidade de festas de casamento participaram");
            System.out.println("4. CPF e nome artístico de cada animador que participou de pelo menos duas festas em 2018");
            System.out.println("5. Nota fiscal de todas as festas e, para as que possuírem, apresentar nome da banda e seu gênero");
            System.out.println("6. Nota fiscal de cada festa e o número de pratos e bebidas oferecedios pela equipe de bufê contratada");
            System.out.println("7. Para cada festa, mostrar nota fiscal, cidade e o número de mesas do local");
            System.out.println("8. Sair");

            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            int idx = -1;

            try {
                idx = Integer.parseInt(input);
            }
            catch (Exception e) {}
            Screen.clear();
            if(idx <= 0 || idx > 8) {
                System.out.println("Comando inválido\n");
                continue;
            }
            else if(idx == 8)
                return;
            else {
                if(idx == 1)
                    this.viewTable();
                else
                    this.generalView(null, idx);
                return;
            }
        }
    }
    
    private void viewTable() {
        ResultSetMetaData rsmd = null;
        while(true) {
            int i = 1;
            System.out.println("Qual tabela gostaria de vizualizar?");
            /* Show every table name and exit option */
            for(String str : this.tableName) {
                System.out.println(i + ". " + str);
                i++;
            }
            System.out.println(i + ". Sair");

            /* Getting input and filtering */
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = filterInsert(input);

            Screen.clear();
            /* Exit */
            if(input.equals(Integer.toString(i)) || input.equals("SAIR"))
                break;
            /* Invalid input */
            else if(input.equals("")) {
                System.out.println("Tabela inválida\n");
            }
            /* Get input from user */
            else {
                this.generalView(input, 1);
            }
        }
    }

    private void createRemoveInput(String tableName) {
        Statement st = null;
        ResultSet rs = null, primaryKey = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0;
        Scanner s = new Scanner(System.in);
        String valor = null;
        int coll = 0;
        DatabaseMetaData meta = null;

        /* Get every table column and number of columns */
        try {
            meta = this.db.getConnection().getMetaData();
            st = this.db.getConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM " + tableName);
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}

        try {
            primaryKey = meta.getPrimaryKeys(null, null, tableName);
        }
        catch (Exception e) {}
        
        while(true) {

            // printar tabela
            generalView(tableName, 1);

            ArrayList<String> pk = new ArrayList<String>();
            
            System.out.println("Digite a linha a ser removida");
            
            /* Prompt to type primary keys */
            try {
                primaryKey.next();
                while(!primaryKey.isAfterLast()) {
                    System.out.print(primaryKey.getString("COLUMN_NAME")+": ");
                    pk.add(s.nextLine());
                    primaryKey.next();
                }
            }
            catch (Exception e) {}
            
            /* Confirmation prompt */
            System.out.println("O dado está correto?");
            System.out.println("1. Sim");
            System.out.println("2. Não");

            String answer = s.nextLine();
            answer = answer.toLowerCase();

            Screen.clear();

            if(answer.equals("1") || answer.equals("sim")) {
                pk = Utils.deAccentArray(pk);
                /* Try to update table */
                int ret = this.db.removeColumn(tableName, pk);
                /* Update success */
                if(ret != 0) {
                    System.out.println("Remoção efetuada com sucesso\n");
                    break;
                }
                /* Error */
                else {
                    System.out.println("Não foi possível remover, deseja tentar de novo?\n");
                    while(true) {
                        System.out.println("1. Sim");
                        System.out.println("2. Não");
                        String ans = s.nextLine();
                       ans = ans.toLowerCase();
                        if(ans.equals("1") || ans.equals("sim")) {
                            System.out.println("Reinsira o dado\n");
                            break;
                        }
                        else if(ans.equals("2") || ans.equals("não"))
                            return;
                        else
                            System.out.println("Resposta inválida\n");
                    }
                }
            }
            /* If data is incorrect */
            else if(answer.equals("2") || answer.equals("não")) {
                System.out.println("O que deseja fazer?");
                while(true) {
                    System.out.println("1. Reinserir dados");
                    System.out.println("2. Sair");

                    String ans = s.nextLine();
                    Screen.clear();
                    if(ans.equals("1") || ans.equals("reinserir dados") ||
                       ans.equals("reinserir")) {
                        System.out.println("Reinsira os dados\n");
                        break;
                    }
                    else if(ans.equals("2") || ans.equals("sair"))
                        return;
                    else
                        System.out.println("Resposta inválida\n");
                }
            }   
        }
    }


    private void remove() {
        while(true) {
            int i = 1;
            System.out.println("Em qual tabela gostaria de fazer a remoção?");
            /* Show every table name and exit option */
            for(String str : this.tableName) {
                System.out.println(i + ". " + str);
                i++;
            }
            System.out.println(i + ". Sair");

            /* Getting input and filtering */
            Scanner s = new Scanner(System.in);
            String tableName = s.nextLine();
            tableName = filterInsert(tableName);

            Screen.clear();
            /* Exit */
            if(tableName.equals(Integer.toString(i)) || tableName.equals("SAIR"))
                break;
            /* Invalid input */
            else if(tableName.equals("")) {
                System.out.println("Tabela inválida\n");
            }
            /* Get input from user */
            else {
                this.createRemoveInput(tableName);
            }
        }
    }
}
