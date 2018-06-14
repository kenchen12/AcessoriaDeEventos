package acessoria;

import java.util.Scanner;
import java.sql.ResultSet;
public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String user, pass;
        System.out.println("Digite o nome de usuário");
        user = s.nextLine();
        System.out.println("Digite a senha");
        pass = s.nextLine();
        DatabaseAcess db = new DatabaseAcess(user, pass);
        ResultSet rs = db.selectCliente();
        try{
            rs.next();
            System.out.println(rs.getString("NOME"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(db.createCliente());
    }
}
