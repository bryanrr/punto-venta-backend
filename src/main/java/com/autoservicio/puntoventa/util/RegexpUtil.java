package com.autoservicio.puntoventa.util;

public class RegexpUtil {
	 public final static String BARCODE="^(?=.{2,30}$)(?![ ])[a-zA-Z0-9]+(?<![_.])$";
	 public final static String DESCRIPTION="^(?=.{2,50}$)[a-zA-Z0-9\\u00f1\\u00d1 ]+(?<![_.])$";
	 public final static String PRICE="^[0-9]\\d{0,4}(\\.\\d{0,2})?$";
	 public final static String USERNAME="^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
	 public final static String PASSWORD="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$";
	 public final static String DATE="^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";
}
