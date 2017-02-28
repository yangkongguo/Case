package towaynet.dao;

import java.util.HashMap;

import towaynet.entity.DingToken;

public interface DingTokenDao {
	public int dingTokenUpdate(HashMap<String, Object>map);
	public DingToken findDingToken();
}
