package org.cmis.portlets.news.taglib;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag allowing to display the files sizes with the right unit : Ko, Mo, Go.
 * 
 * @author Anyware Services - Delphine Gavalda 19 oct. 2010
 */
public class FileSizeConverterTag extends SimpleTagSupport {

    private static final int KB = 1024;
    private static final double KB_DEC = 1024.0;
    private static final long MB = KB * KB;
    private static final long GB = MB * KB;

    /* displayed format */
    private static DecimalFormat oneDecimal = new DecimalFormat("0.0");

    /* Tag parameter */
    private long value;

    /**
     * Constructor.
     */
    public FileSizeConverterTag() {
	super();
    }

    /**
     * @param val
     */
    public void setValue(final long val) {
	this.value = val;
    }

    @Override
    public void doTag() throws JspException, IOException {
	try {

	    getJspContext().getOut().print(format(this.value));

	} catch (IOException e) {
	    throw new JspException("I/O Error", e);
	}
    }

    /**
     * Format a file size, add the right unit.
     * 
     * @param number
     * @return String
     */
    public static String format(final long number) {
	long absNumber = Math.abs(number);
	double result = number;
	String suffix = "";
	if (absNumber < KB) {
	    suffix = " Octets";
	    return number + suffix;
	    
	} else if (absNumber < MB) {
	    result = number / KB_DEC;
	    suffix = " Ko";
	    
	} else if (absNumber < GB) {
	    result = number / (KB_DEC * KB);
	    suffix = "Mo";
	    
	} else {
	    result = number / (KB_DEC * MB);
	    suffix = "Go";
	}
	return oneDecimal.format(result) + suffix;
    }

}
