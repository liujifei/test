package lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class test {

    public static void main(String[] args) {
        sort();
    }

    public static void sort() {
        List<Integer> list = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            Integer k = random.nextInt();
            System.out.println(k);
            list.add(k);
        }
//        Collections.sort(list, (Integer x, Integer y) -> x.compareTo(y));
        System.out.println(list);
    }
    public static void sort1() {
        List<Integer> list = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            Integer k = random.nextInt();
            System.out.println(k);
            list.add(k);
        }
//        list.sort(Comparator.comparing(Integer i);
    }
}
