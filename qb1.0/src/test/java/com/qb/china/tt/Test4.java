package com.qb.china.tt;

/**
 * Exception summary
 * Create by Long.Meng on 2016年9月26日.
 */
public class Test4 {
	int $slw;

	private String id;

	public void area() throws Exception {
		System.out.println("asdf");
		String string = null;
		System.out.println(string.toString());
	}

	public void print(String id) {
		if (id.length() == 7) {
			this.id = id;
		} else {
			throw new IllegalArgumentException("参数长度不是7位");
		}
	}

	public static void main(String[] args) {
		Test4 t = new Test4();

		
		System.out.println();
		try {
			t.print("aa");
			t.area();
		} catch (Exception e) {
			System.out.println("error");
			//e.printStackTrace();
		}
	}
}
