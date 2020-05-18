package com.czkj.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * @author zhangqh
 * @date 2020-04-02
 */
public class ModifyHttpServletRequestWrapper extends HttpServletRequestWrapper
{
    private Map<String, String> customHeaders;

    public ModifyHttpServletRequestWrapper(HttpServletRequest request)
    {
        super(request);
        this.customHeaders = new HashMap();
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public String getHeader(String name)
    {
        String value = (String)this.customHeaders.get(name);
        if (value != null) {
            return value;
        }
        return ((HttpServletRequest)getRequest()).getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        Set<String> set = new java.util.HashSet(this.customHeaders.keySet());
        Enumeration<String> enumeration = ((HttpServletRequest)getRequest()).getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = (String)enumeration.nextElement();
            set.add(name);
        }
        return Collections.enumeration(set);
    }
}
