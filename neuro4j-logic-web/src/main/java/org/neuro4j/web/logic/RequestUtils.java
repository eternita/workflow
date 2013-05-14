package org.neuro4j.web.logic;

public class RequestUtils 
{
	
	// from jstl lib (c:redirect)
	public static boolean isAbsoluteUrl(String url) 
	{
		if (url == null)
			return false;
		int colonPos;
		if ((colonPos = url.indexOf(":")) == -1) {
			return false;
		}

		for (int i = 0; i < colonPos; ++i) {
			if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+.-".indexOf(url.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}
}
