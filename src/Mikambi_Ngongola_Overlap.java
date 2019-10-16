public class Mikambi_Ngongola_Overlap {

  static class Line {
    int start,end;

    //Lines are sanitized in the constructor, to ensure start is always before the end
    Line(int start, int end) {
      if(end < start) {
        this.start = end;
        this.end = start;
      } else {
        this.start = start;
        this.end = end;
      }
    }
  }

  public static void main(String[] args) {
    //Test 0
    Line l1 = new Line(1,5);
    Line l2 = new Line(2,6);
    //Test 1
    Line l3 = new Line(1,5);
    Line l4 = new Line(6,8);
    //Test 2
    Line l5 = new Line(1,1);
    Line l6 = new Line(1,1);
    //Test 3
    Line l7 = new Line(-3,-1);
    Line l8 = new Line(0,5);
    //Test 4
    Line l9 = new Line(5,1);
    Line l10 = new Line(8,5);

    boolean[] overlap = new boolean[]{overlap(l1,l2),overlap(l3,l4),overlap(l5,l6),overlap(l7,l8),overlap(l9,l10)};

    for(int i=0; i<overlap.length;i++) {
      if(overlap[i]) {
        System.out.println("Test " + i + ": Lines overlap");
      } else {
        System.out.println("Test " + i + ": Lines do not overlap");
      }
    }
  }

  private static boolean overlap(Line l1, Line l2) {
    //As a design decision, two lines overlap if they share a point
    if(l1.start <= l2.start) {
      return l2.start <= l1.end;
    } else {
      return l1.start <= l2.end;
    }
  }

}
