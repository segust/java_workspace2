package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomUtil {
	
	public static String getUuidFromDom(String dom){
		Pattern pattern = Pattern.compile("/u/[0-9]+.aspx");
		Matcher matcher = pattern.matcher(dom);
		if(matcher.find()){
			String temp = matcher.group();
			pattern = Pattern.compile("[0-9]+");
			matcher = pattern.matcher(dom);
			if(matcher.find()){
				return matcher.group();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
}
