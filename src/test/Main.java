package test;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()){
			String tempLine = scan.nextLine();
			Pattern p = Pattern.compile("\\s+");
			Matcher m = p.matcher(tempLine);
			tempLine= m.replaceAll(" ");
			String[] a = tempLine.split(" ");
			int[] aa ={0,0,0,0};
			for(int i=0; i<4;i++){
				aa[i]= Integer.valueOf(a[i]);
			}
			if((aa[1]+aa[2]>aa[3]&&aa[1]+aa[3]>aa[2]&&aa[2]+aa[3]>aa[1])
					 ||(aa[1]+aa[2]>aa[0]&&aa[1]+aa[0]>aa[2]&&aa[2]+aa[0]>aa[1])
					 ||(aa[0]+aa[2]>aa[3]&&aa[0]+aa[3]>aa[2]&&aa[2]+aa[3]>aa[0])
					 ||(aa[1]+aa[0]>aa[3]&&aa[1]+aa[3]>aa[0]&&aa[0]+aa[3]>aa[1])){
				 System.out.println("triangle");
			 } else if (aa[0]+aa[1]==aa[2]
					 ||aa[0]+aa[1]==aa[3]
					 ||aa[0]+aa[2]==aa[3]
					 ||aa[1]+aa[2]==aa[3]){
				 System.out.println("segment");
			 } else {
				 System.out.println("impossible");
			 }
		}
	}
}
