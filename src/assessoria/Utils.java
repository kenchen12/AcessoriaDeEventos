package assessoria;

import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.List;

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
  /*
  public static String formatAsTable(List<List<String>> rows){
    int[] maxLengths = new int[rows.get(0).size()];
    for (List<String> row : rows){
        for (int i = 0; i < row.size(); i++){
            maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
        }
    }

    StringBuilder formatBuilder = new StringBuilder();
    for (int maxLength : maxLengths){
        formatBuilder.append("%-").append(maxLength + 2).append("s");
    }
    String format = formatBuilder.toString();

    StringBuilder result = new StringBuilder();
    for (List<String> row : rows){
        result.append(String.format(format, row.toArray(new String[0]))).append("\n");
    }
    return result.toString();
  }
  */
  public static String formatOutput(List<List<String>> output){
    int[] maxLengths = new int[output.get(0).size()];
    for (List<String> row : output){
        for (int i = 0; i < row.size(); i++){
            if(row.get(i) != null)
              maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
        }
    }
    StringBuilder formatBuilder = new StringBuilder();
    for (int maxLength : maxLengths){
        formatBuilder.append("%-").append(maxLength + 2).append("s");
    }
    String format = formatBuilder.toString();
    StringBuilder result = new StringBuilder();
    for (List<String> row : output){
        result.append(String.format(format, row.toArray())).append("\n");
    }
    return result.toString();
  }
}
