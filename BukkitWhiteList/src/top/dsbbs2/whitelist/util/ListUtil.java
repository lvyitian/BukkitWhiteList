package top.dsbbs2.whitelist.util;

import java.util.ArrayList;

public class ListUtil {
	@SafeVarargs
	public static <T> ArrayList<T> toList(T... o)
	{
		return new ArrayList<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8461643752862517970L;

			{
				for(T i : o)
				  this.add(i);
			}
		};
	}
}
