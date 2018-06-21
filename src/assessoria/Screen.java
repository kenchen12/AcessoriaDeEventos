package assessoria;

public class Screen {

    
    public static void clear() {
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            catch(Exception e) {}
        }
        else {
            System.out.print("\033[H\033[2J");
        }
    }
}
