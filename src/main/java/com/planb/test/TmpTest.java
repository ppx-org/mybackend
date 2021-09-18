package com.planb.test;

import java.util.BitSet;

public class TmpTest {
	
	public static void main(String[] args) {
		System.out.println("0000");
		BitSet bs = new BitSet();
		bs.set(10000);
		System.out.println("..:" + bs.size()); // 10048
		
	}

}
