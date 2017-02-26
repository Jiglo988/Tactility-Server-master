package org.hyperion.rs2.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arsen Maxyutov.
 */
public class ExtraData {

	/**
	 * The default String value.
	 */
	public static final String DEFAULT_STRING_VALUE = null;

	/**
	 * The default int value.
	 */
	public static final int DEFAULT_INT_VALUE = 0;

	/**
	 * The default boolean value.
	 */
	public static final boolean DEFAULT_BOOL_VALUE = Boolean.FALSE;

	/**
	 * Holds extra Data.
	 */
	private final Map<String, Object> extraData;
	
	public ExtraData() {
		extraData = new HashMap<>();
	}

	/**
	 * Puts an element in the extra data map.
	 *
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		extraData.put(key, value);
	}

	/**
	 * Removes the element from the extra data map.
	 *
	 * @param key
	 */
	public void remove(String key) {
		extraData.remove(key);
	}

	/**
	 * Gets a string from the extra data map.
	 *
	 * @param key
	 * @return the string value
	 */
	public String getString(String key) {
		Object value = extraData.get(key);
		if(value == null) return DEFAULT_STRING_VALUE;
		return (String) value;
	}

	/**
	 * Gets the integer value from the extra data map, if the value is not found 0 is returned.
	 *
	 * @param key
	 * @return the int value
	 */
	public int getInt(String key) {
		Object value = extraData.get(key);
		if(value == null) return DEFAULT_INT_VALUE;
		return value instanceof String ? Integer.parseInt(value.toString()) : (Integer)value;
	}


	/**
	 * Gets the long value from the extra data map, if the value is not found 0 is returned.
	 *
	 * @param key
	 * @return the long value
	 */
	public long getLong(String key) {
		Object value = extraData.get(key);
		if(value == null) return DEFAULT_INT_VALUE;
		return value instanceof String ? Long.valueOf(value.toString()) :(Long) value;
	}

	/**
	 * Gets the boolean from the extra data map, if the value is not found <code>false</code> is returned.
	 *
	 * @param key
	 * @return the boolean value
	 */
	public boolean getBoolean(String key) {
		Object value = extraData.get(key);
		if(value == null) return DEFAULT_BOOL_VALUE;
		return value instanceof String ? Boolean.valueOf(value.toString()) : (Boolean) value;
	}

	/**
	 * Gets the Object value from the extra data map.
	 *
	 * @param key
	 * @return the value
	 */
	public Object get(String key) {
		return extraData.get(key);
	}

    /**
     * Enables saving of extradata inside the player file
     */
    public String getSaveableString() {
        return extraData.toString().replace("=", "-");
    }

    public void parse(String saved) {
        if(saved == null || saved.length() < 4)
            return;
        try {
            saved = saved.replace('{', '\u0000').replace("}", "").trim();
            final String parts[] = saved.split(", ");
            for(final String s : parts) {
                try {
                    //{christmasevent=2, zombiewave=0}
                    //System.out.println(s);
                    final String p[] = s.split("-");
                    extraData.put(p[0], p[1].trim());
                }catch(Exception e) {
                    //e.printStackTrace();
                }
            }
        } catch(Exception e) {
            //e.printStackTrace();
        }
    }



}
