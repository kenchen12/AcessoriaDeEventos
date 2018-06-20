package assessoria;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        if(this.db == null)
            return;
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
				this.update();
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
    
    private void createInsertInput(String tableName) {
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0;
        Scanner s = new Scanner(System.in);
        String aux = null;
        ArrayList<String> input = new ArrayList<String>();
        
        try {
            st = this.db.getConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM " + tableName);
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}
        
        System.out.println("Digite os dados a serem inseridos");

        while(true) {
            for(int col = 1; col <= nCols; col++) {
                String name = null;
                try {
                    name = rsmd.getColumnName(col);
                }
                catch (Exception e) {}

                while(true) {
                    System.out.print(name + ": ");
                    aux = s.nextLine();
                    int nullable = 0;
                    try {
                        nullable = rsmd.isNullable(col);
                    }
                    catch(Exception e) {}
                    if(aux.equals("") && nullable == ResultSetMetaData.columnNoNulls) {
                        System.out.println("Valor inválido. Este campo é obrigatório");
                    }
                    else {
                        input.add(aux);
                        break;
                    }
                }
            }

            System.out.println("Os dados estão corretos?");
            System.out.println("1. Sim");
            System.out.println("2. Não");

            String answer = s.nextLine();
            answer = answer.toLowerCase();

            if(answer.equals("1") || answer.equals("sim")) {
                int ret = this.db.insertColumn(tableName, input);
                if(ret != 0) {
                    System.out.println("Inserção efetuada com sucesso");
                    break;
                }
                else {
                    System.out.println("Não foi possível inserir, deseja inserir de novo?");
                    while(true) {
                        System.out.println("1. Sim");
                        System.out.println("2. Não");
                        String ans = s.nextLine();
                        ans = ans.toLowerCase();
                        if(ans.equals("1") || ans.equals("sim")) {
                            System.out.println("Reinsira os dados");
                            input.clear();
                            break;
                        }
                        else if(ans.equals("2") || ans.equals("não"))
                            return;
                        else
                            System.out.println("Resposta inválida");
                    }
                }
            }
            else if(answer.equals("2") || answer.equals("não")) {
                System.out.println("O que deseja fazer?");
                while(true) {
                    System.out.println("1. Reinserir dados");
                    System.out.println("2. Sair");

                    String ans = s.nextLine();
                    if(ans.equals("1") || ans.equals("sim")) {
                        System.out.println("Reinsira os dados");
                        input.clear();
                        break;
                    }
                    else if(ans.equals("2") || ans.equals("não"))
                        return;
                    else
                        System.out.println("Resposta inválida");
                }
            }
        }            
    }
    
    
    
    private void insert() {
        while(true) {
            int i = 1;
            System.out.println("Em qual tabela gostaria de inserir?");
            for(String str : this.tableName) {
                System.out.println(i + ". " + str);
                i++;
            }
            System.out.println(i + ". RETORNAR");
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = filterInsert(input);

            if(input.equals(Integer.toString(i)) || input.equals("SAIR"))
                break;
            else if(input.equals("")) {
                System.out.println("Tabela inválida");
            }
            else {
                this.createInsertInput(input);
            }
        }
    }
    
    
    private void createUpdateInput(String tableName) {
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int nCols = 0;
        Scanner s = new Scanner(System.in);
        String aux = null, aux2 = null;
        ArrayList<String> input = new ArrayList<String>();
        
        try {
            st = this.db.getConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM " + tableName);
            rsmd = rs.getMetaData();
            nCols = rsmd.getColumnCount();
        }
        catch (Exception e) {}
        
        System.out.println("Digite a coluna que deseja alterar O NOME");
        for(int col = 1; col <= nCols; col++) {
                String name = null;
                try {
                    name = rsmd.getColumnName(col);
                }
                catch (Exception e) {}
				System.out.println(col + ". " + name);
        }
            
        String scoluna = s.nextLine();
        int coluna = Integer.parseInt(scoluna);
        
        while (true){	
			
			while (true){
				
				System.out.println("Digite o cpf do cliente a ser alterado");
				aux = s.nextLine();
				
				System.out.println("Digite o novo nome a ser inserido");
				aux2 = s.nextLine();
				
				
				int nullable = 0;
				try {
					nullable = rsmd.isNullable(coluna);
				}
				catch(Exception e) {}
				if(aux.equals("") && nullable == ResultSetMetaData.columnNoNulls) {
					System.out.println("Valor inválido. Este campo é obrigatório");
				}
				else {
					break;
				}
			}
				
				
			System.out.println("O novo dado está correto?");
			System.out.println("1. Sim");
			System.out.println("2. Não");

			String answer = s.nextLine();
			answer = answer.toLowerCase();

			if(answer.equals("1") || answer.equals("sim")) {
				int ret = this.db.updteColumn1(tableName, aux, aux2);
				if(ret != 0) {
					System.out.println("Inserção efetuada com sucesso");
					break;
				}
				else {
					System.out.println("Não foi possível inserir, deseja inserir de novo?");
					while(true) {
						System.out.println("1. Sim");
						System.out.println("2. Não");
						String ans = s.nextLine();
						ans = ans.toLowerCase();
						if(ans.equals("1") || ans.equals("sim")) {
							System.out.println("reinsira o dado");
							break;
						}
						else if(ans.equals("2") || ans.equals("não"))
							return;
						else
							System.out.println("Resposta inválida");
					}
				}
			}
			else if(answer.equals("2") || answer.equals("não")) {
				System.out.println("O que deseja fazer?");
				while(true) {
					System.out.println("1. Reinserir dados");
					System.out.println("2. Sair");

					String ans = s.nextLine();
					if(ans.equals("1") || ans.equals("sim")) {
						System.out.println("Reinsira os dados");
						break;
					}
					else if(ans.equals("2") || ans.equals("não"))
						return;
					else
						System.out.println("Resposta inválida");
				}
			}
        }
    }
    
    
    
    
    
    private void update() {
        while(true) {
            int i = 1;
            System.out.println("Qual tabela gostaria de atualizar?");
            for(String str : this.tableName) {
                System.out.println(i + ". " + str);
                i++;
            }
            System.out.println(i + ". RETORNAR");
            Scanner s = new Scanner(System.in);
            String input = s.nextLine();
            input = filterInsert(input);

            if(input.equals(Integer.toString(i)) || input.equals("SAIR"))
                break;
            else if(input.equals("")) {
                System.out.println("Tabela inválida");
            }
            else {
                this.createUpdateInput(input);
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
}
