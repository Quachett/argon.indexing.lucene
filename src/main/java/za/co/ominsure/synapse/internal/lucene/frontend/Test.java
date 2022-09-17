package za.co.ominsure.synapse.internal.lucene.frontend;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
	third();

	}
	
	private static void third() {
		String s = "cliffoclmifdpnsfnpd";
		String s1 = "aabbccdeeffghhiijkkl";
		String s2 = "1122334455678889";
		
		List<String> list = new ArrayList<>();
		
		char[] chars = s2.toCharArray();
		
		for(char c : chars) {
			if(list.contains(String.valueOf(c)))
				list.remove(String.valueOf(c));
			else
				list.add(String.valueOf(c));
		}
		if(list.size()>=3)
			System.out.println(list.get(2));
		else
			System.out.println("Not found");
	}

}
