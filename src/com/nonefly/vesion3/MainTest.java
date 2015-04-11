package com.nonefly.vesion3;

public class MainTest {
	public static void main(String[] args) {
		/*创建词法分析类*/
		TestLexer testLexer = new TestLexer("./src/input.txt");
		//FileUtil.clearFile();//清空文件
		testLexer.analyse();
	}
}
