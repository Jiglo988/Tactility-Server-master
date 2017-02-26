package org.hyperion.rs2.model;

import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Time;

import java.text.DecimalFormat;

public class Ban {

	private String name;

	private String ip;

	private boolean byIp;

	private long expiration_time;
	
	private String reason = null;

	private int type;

	public Ban(String line) {
		String[] parts = line.split(",");
		setName(parts[0]);
		setIp(parts[1]);
		boolean byIp = Boolean.parseBoolean(parts[2]);
		setByIp(byIp);
		long expiration_time = Long.parseLong(parts[3]);
		setExpirationTime(expiration_time);
		int type = Integer.parseInt(parts[4]);
		setType(type);
	}

	/**
	 * @param name
	 * @param ip
	 * @param byIp
	 * @param expiration_time The time in milliseconds after which the ban is no longer applied.
	 * @param type
	 */
	public Ban(String name, String ip, boolean byIp, long expiration_time, int type) {
		this(name, ip, byIp, expiration_time,type,null);
	}
	
	public Ban(String name, String ip, boolean byIp, long expiration_time, int type, String reason) {
		this.setName(name);
		this.setIp(ip);
		this.setByIp(byIp);
		this.setExpirationTime(expiration_time);
		this.setType(type);
		this.reason = reason;
	}

	/**
	 * @return The expiration time in milliseconds.
	 */
	public long getExpirationTime() {
		return expiration_time;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setExpirationTime(long duration) {
		this.expiration_time = duration;
	}

	/**
	 * @return the byIp
	 */
	public boolean isByIp() {
		return byIp;
	}
	
	
	
	public String getReason()
	{
		if(reason == null)
			return "Undefined";
		return reason;
	}
	/**
	 * @param byIp the byIp to set
	 */
	public void setByIp(boolean byIp) {
		this.byIp = byIp;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		//System.out.println("Setting ban type: " + type);
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	
	public static final DecimalFormat HOURS = new DecimalFormat("##0.##");
	
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "[@blu@"+ TextUtils.titleCase(name) + "@bla@] | [IP:"
				+ (ip == null ? "null" : ip.substring(0, ip.lastIndexOf('.'))) + "] | [By Ip?:" + byIp + "] | [Type:" + type + "] | [Left Time:" + HOURS.format(getHours())+"Hours]";
	}
	
	public long getHours() {
		long time =  expiration_time - System.currentTimeMillis();
		return time/Time.ONE_HOUR;
	}

	@Override
	public boolean equals(Object other) {
		if(! (other instanceof Ban))
			return false;
		Ban otherban = (Ban) other;
		if(! name.equals(otherban.getName()))
			return false;
		if(! ip.equals(otherban.getIp()))
			return false;
		if(type != otherban.getType())
			return false;
		if(byIp != otherban.isByIp())
			return false;
		if(expiration_time != otherban.getExpirationTime())
			return false;
		return true;
	}

}
