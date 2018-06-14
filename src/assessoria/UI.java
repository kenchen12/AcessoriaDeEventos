package assessoria;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Statement;
import assessoria.DatabaseAccess;

public class UI {

    DatabaseAccess db;
    ArrayList<String> tableName;

    public UI() {
        this.db = this.Connect();
        this.tableName = this.setTableNames();
    }

    private DatabaseAccess Connect() {
        DatabaseAccess db = null;
        while(true) {
            Scanner s = new Scanner(System.in);
            String user, pass;
            System.out.println("Conecte-se ao banco de dados");
            System.out.println("(Digite um usuário vazio para sair)");
            System.out.print("Digite o nome de usuário: ");
            user = s.nextLine();
            if(user == null || user.isEmpty())
                break;
            System.out.print("Digite a senha: ");
            pass = s.nextLine();
            db = new DatabaseAccess(user, pass);
            if(db.getConnection() != null) {
                System.out.println("Conexão efetuada com sucesso");
                break;
            }
            else {
                System.out.println("Não foi possível conectar ao banco de dados");
            }
        }
        return db;
    }

    private ArrayList<String> setTableNames() {
        Statement st;
        ResultSet rs = null;
        ArrayList<String> tn = new ArrayList<String>();
        try {
            st = db.getConnection().createStatement();
            rs = st.executeQuery("SELECT TABLE_NAME FROM USER_TABLES");
        }
        catch (Exception e) {}
        try {
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

    public void mainMenu() {
        while(true) {
            System.out.println("O que deseja fazer?");
            System.out.println("1. Inserir");
            System.out.println("2. Atualizar");
            System.out.println("3. Visualizar");
            System.out.println("4. Remover");
            System.out.println("5. Sair");

            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = this.filterMainMenu(input);

            if(input.equals("inserir")) {
                this.insert();
            }
            else if(input.equals("atualizar")) {

            }
            else if(input.equals("visualizar")) {

            }
            else if(input.equals("remover")) {

            }
            else if(input.equals("sair")) {
                break;
            }
            else {
                System.out.println("Comando inválido");
            }
        }
    }

    private String filterInsert(String raw) {
        raw = raw.toUpperCase();
        int idx = -1;
        try {
            idx = Integer.parseInt(raw);
        }
        catch(Exception e) {}

        if(idx == this.tableName.size()+ 1 || raw.equals("SAIR")) {
            return "SAIR";
        }

        if(idx != -1 && idx <= this.tableName.size() && idx > 0)
            return this.tableName.get(idx-1);

        if(this.tableName.contains(raw))
            return raw;
        else
            return "";
    }

    private void insert() {
        while(true) {
            int i = 1;
            System.out.println("Em qual tabela gostaria de inserir?");
            for(String str : this.tableName) {
                System.out.println(i + ". " + str);
                i++;
            }
            System.out.println(i + ". Sair");
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = filterInsert(input);
            System.out.println("Input = '" + input + "'");

            if(input.equals(Integer.toString(i)) || input.equals("SAIR"))
                break;
            else if(input.equals("")) {
                System.out.println("Tabela inválida");
            }
            else {
                // inserir
            }
        }
    }
}
