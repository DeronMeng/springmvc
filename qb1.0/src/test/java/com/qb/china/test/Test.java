package com.qb.china.test;

/**
 * 
 * Create by Long.Meng on 2016年9月21日.
 */
 public class Test {
	int y = 0;
	/**
	 * 
	 */
	public Test() {
		super();
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		int j=0;
		while(j<0) {
			System.out.print(j + " ");
			j++;
		}
		
		do {
			System.out.print(j + " ");
			j++;
		} while (j<0);
		System.out.println();
		for (int i = 1; i <= 9; i++) {
			for (int k = 1; k <= i; k++) {
				System.out.print(i + "*" + k + "=" + (i * k) + "\t");
			}
			System.out.println();
		}
		
	}

}
 
 class Text{
	 public static void main(String[] args) {
		System.out.println("fuck");
	}
 }
