package com.planb.security.permission;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class PermissionCache {
	
	public static int authCacheVersion = -1;
	
	private static Map<String, Integer> uriMap = new HashMap<String, Integer>();
	
	private static Map<Integer, BitSet> roleBitSet = new HashMap<Integer, BitSet>();
	
	
	public static Map<String, Integer> getUriMap() {
		return uriMap;
	}
	
	public static Map<Integer, BitSet> getRoleBitSet() {
		return roleBitSet;
	}
	
	public static void loadUri(String uri, Integer uriId) {
		uriMap.put(uri, uriId);
	}
	
	public static void loadRoleRes(Integer uriId, BitSet resBitSet) {
		roleBitSet.put(uriId, resBitSet);
	}
	
	
}
