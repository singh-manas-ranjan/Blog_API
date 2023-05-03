package com.api_blog.utils;

import jakarta.servlet.http.HttpServletRequest;

public class SiteUrl {
	public static String getSiteUrl(HttpServletRequest request)
	{
		String scheme=request.getScheme();
		String host=request.getHeader("Host");
		String siteUrl = request.getContextPath();
			
		return scheme+"://"+host+siteUrl;
	}
}
