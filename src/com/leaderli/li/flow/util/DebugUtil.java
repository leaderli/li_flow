package com.leaderli.li.flow.util;

public class DebugUtil {

	public static void print(int depth) {
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		if (depth < 0) {
			depth = stacks.length;
		}
		System.out.println("----------------------------------------------begin");
		for (int i = 2; i < depth; i++) {
			System.out.println(stacks[i]);
		}
	}
}
