public class Mikambi_Ngongola_StringCompare {

  public static void main(String[] args) {
    System.out.println(compare("1.2", "1.1"));
    System.out.println(compare("1.1", "1.2"));
    System.out.println(compare("1.0", "1.0"));
    System.out.println(compare("2.0", "1.01"));
    System.out.println(compare("1.0.0", "1.0.1"));
    System.out.println(compare("0.0", "0.0000"));
  }

  private static String compare(String num1, String num2) {
    String sanNam1 = num1.replace(".","");
    String sanNam2 = num2.replace(".","");
    if(sanNam1.length() != sanNam2.length()) {
      if (sanNam1.length() > sanNam2.length()) {
        sanNam2 = padLength(sanNam2, sanNam1.length()-sanNam2.length());
      } else {
        sanNam1 = padLength(sanNam1,sanNam2.length()-sanNam1.length());
      }
    }
    int d1 = Integer.parseInt(sanNam1);
    int d2 = Integer.parseInt(sanNam2);

    if(d1 <0 || d2<0) {
      return "Error: Input version contains negative number!";
    }

    return num1 + " is " + (d1 > d2 ? "greater than " : (d1==d2 ? "equal to " : "less than " )) + num2;
  }

  private static String padLength(String input, int padding) {
    StringBuilder stringBuilder = new StringBuilder(input);
    for (int i = 0; i < padding; i++) {
      stringBuilder.append("0");
    }
    return stringBuilder.toString();
  }
}
