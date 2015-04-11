package com.nonefly.vesion1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * java 词法分析器
 * 读文件，结果输出未保存
 * @author zhangyu
 * @version 1.0
 */
public class Analyzer {
	private String keyWords[] = { "abstract", "boolean", "break", "byte",
			"case", "catch", "char", "class", "continue", "default", "do",
			"double", "else", "extends", "final", "finally", "float", "for",
			"if", "implements", "import", "instanceof", "int", "interface",
			"long", "native", "new", "package", "private", "protected",
			"public", "return", "short", "static", "super", "switch",
			"synchronized", "this", "throw", "throws", "transient", "try",
			"void", "volatile", "while", "strictfp","enum","goto","const","assert" }; // 关键字数组
	private char operators[] = { '+', '-', '*', '/', '=', '>', '<', '&' }; // 运算符数组
	private char separators[] = { ',', ';', '{', '}', '(', ')', '[', ']', '_',
			':', '、', '.', '"' }; // 分隔符数组
	private String fileSrcName; // 源程序名
	private StringBuffer buffer = new StringBuffer(); // 缓冲区
	private char ch; // 字符变量，存放最新读进的源程序字符
	private static int i = 0;
	private static int ketType ;
	private String strToken; // 字符数组，存放构成单词符号的字符串

	public Analyzer() {
	}

	public Analyzer(String fileSrcName) {
		this.fileSrcName = fileSrcName;
	}

	/**
	 * 将下一个输入字符读到ch中，搜索指示器前移一个字符
	 */
	public void getChar() {
		ch = buffer.charAt(i);
		i++;
	}

	/**
	 * 检查ch中的字符是否为空白，若是则调用getChar() 直至ch中进入一个非空白字符
	 */
	public void getBc() {
		while (Character.isSpaceChar(ch))
			getChar();
	}

	/**
	 * 将ch连接到strToken之后
	 */
	public void concat() {
		strToken += ch;
	}

	/**
	 * 判断字符是否为字母
	 */
	boolean isLetter() {
		return Character.isLetter(ch);
	}

	/**
	 * 判断字符是否为数字
	 */
	boolean isDigit() {
		return Character.isDigit(ch);
	}

	/**
	 * 将搜索指示器回调一个字符位置，将ch值为空白字
	 */
	public void retract() {
		i--;
		ch = ' ';
	}

	/**
	 * 判断单词是否为关键字
	 * [1,50]
	 */
	public int isKeyWord() {
		ketType = -1;
		for (int i = 0; i < keyWords.length; i++) {
			if(keyWords[i].equals(strToken)) ketType = i+1 ;
		}
		return ketType;
	}

	/**
	 * 判断是否为运算符
	 * [51,100)
	 */
	public int isOperator() {
		ketType = -1;
		for (int i = 0; i < operators.length; i++) {
			if(ch == operators[i]) ketType = i + 51;
		}
		return ketType;
	}

	/**
	 * 判断是否为分隔符
	 * [101,150)
	 */
	public int isSeparators() {
		ketType = -1;
		for (int i = 0; i < separators.length; i++) {
			if (ch == separators[i]) ketType = i+101;
		}
		return ketType;
	}

	/**
	 * 将strToken插入到关键字表
	 */
	public void insertKeyWords(String strToken) {
		//System.out.print("关键字，种别[1,50]");
		System.out.println("=====("+ketType+","+strToken+")");
	}

	/**
	 * 将strToken插入到符号表
	 */
	public void insertId(String strToken) {
		//System.out.print("标识符，种别200");
		System.out.println("=====("+200+","+ch+")");
	}

	/**
	 * 将strToken中的常数插入到常数表中
	 */
	public void insertConst(String strToken) {
		int num = Integer.parseInt(strToken);
		//System.out.print("常数，种别0");
		System.out.println("=====("+0+","+strToken+")");
	}

	/**
	 * 将ch插入到运算符表中
	 */
	public void insertOperators(char ch) {
		//System.out.print("运算符，种别 [51,100)");
		System.out.println("=====("+ketType+","+ch+")");
	}

	/**
	 * 将ch插入到分隔符表
	 */
	public void insertSeparators() {
		//System.out.print("分隔符，种别 [101,150)");
		System.out.println("=====("+ketType+","+ch+")");
	}

	/**
	 * 将源程序读入到缓冲区中
	 */
	public void readFile() {
		try {
			FileReader fis = new FileReader(this.fileSrcName);
			BufferedReader br = new BufferedReader(fis);
			String temp = null;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
			}

		} catch (FileNotFoundException e) {
			System.out.println("源文件未找到!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读写文件出现异常!");
			e.printStackTrace();
		}
	}

	/**
	 * 词法分析
	 */
	public void analyse() {
		boolean isCode, value;
		strToken = ""; // 置strToken为空串
		while (i < buffer.length()) {
			getChar();
			getBc();
			if (isLetter()) { // 如果ch为字母
				while (isLetter() || isDigit()) {
					concat();
					getChar();
				}
				retract(); // 回调
				if (isKeyWord()>0) { // 如果是为关键字，则插入到1.保留字表中
					insertKeyWords(strToken);
				} else { // 否则插入到2.符号表中
					insertId(strToken);
				}
				strToken = "";
			} else if (isDigit()) { // 如果ch为数字
				while (isDigit()) {
					concat();
					getChar();
				}
				retract(); // 回调
				insertConst(strToken); // 是常数，插入到3.常数表中
				strToken = "";
			} else if (isOperator()>0) { // 如果是运算符，则插入到4.运算符表
				insertOperators(ch);
			} else if (isSeparators()>0) { // 如果是分隔符，插入到5.分隔符表中
				insertSeparators();
			}
		}
	}

	public static void main(String[] args) {
		Analyzer alr = new Analyzer("./src/input.txt");//文件路径
		alr.readFile();
		alr.analyse();
		System.err.println("常数，种别0");
		System.err.println("关键字，种别[1,50]");
		System.err.println("运算符，种别 [51,100)");
		System.err.println("分隔符，种别 [101,150)");
		System.err.println("标识符，种别200");
	}
}