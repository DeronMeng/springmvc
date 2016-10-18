package com.qb.china.test;

/**
 * 
 * Create by Long.Meng on 2016年9月26日.
 */
public class Test2 {
	Test t = new Test();
	Text text = new Text();
	
	public static void main(String[] args) {
		String m=" ";
	    for(int a=0;a<4;a++){//控制行数，为后面的公式定基数
	        for(int b=4-a;b>=0;b--){            
	            System.out.print(m);//输出*之前的空格
	        }for(int c=2*a-1;c>-2;c--){
	            System.out.print("*");//输出对应行数的*数目
	        }
	        System.out.println();//换行
	    }
	    for(int A=4;A<7;A++){//控制行数，为后面的公式定基数
	        for(int B=A-3;B>=-1;B--){
	            System.out.print(m);//输出*之前的空格
	        }for(int C=13-2*A;C>0;C--){
	            System.out.print("*");//输出对应行数的*数目
	        }
	        System.out.println();//换行
	    }
	}
}
