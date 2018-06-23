package assessoria;

import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Utils{
  public static String deAccent(String str) {
    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(nfdNormalizedString).replaceAll("").toUpperCase();
  }

  public static void main(String[] args){
    Scanner s = new Scanner(System.in);
    String input = s.nextLine();
    System.out.println(Utils.deAccent(input));
  }
}
