package com.dongnaoedu.reg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonRegTest {

	public static void main(String[] args) {
		String json = "{name:\"jason\",father:\"jason\",age:18}";
		
		//name:"jason"
		//age:18
		//\"\\w+\" 字符串属性
		Pattern p = Pattern.compile("\\w+:(\"\\w+\"|\\d*)");
		Matcher m = p.matcher(json);
		while(m.find()){
			String text = m.group();
			int dotPos= text.indexOf(":");
			String key = text.substring(0, dotPos);
			String value = text.substring(dotPos+1, text.length());
			//替换字符串的开始结束的双引号
			value = value.replaceAll("^\\\"|\\\"$", "");
			System.out.println(key);
			System.out.println(value);
		}
	}

}
