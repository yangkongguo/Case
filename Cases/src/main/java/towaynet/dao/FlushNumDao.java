package towaynet.dao;

import java.util.HashMap;

import towaynet.entity.FlushNum;

public interface FlushNumDao {
	public int flushUpdate(HashMap<String, Object>map);
	public FlushNum findFlushNum();
}
