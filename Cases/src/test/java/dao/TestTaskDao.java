package dao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;


public class TestTaskDao {
	
	@Test
	public void test1() throws Throwable{
		List<String>list=new ArrayList<String>();
		list.add("e8d61157f911480db3d8fd1ad2706c34");
		String str="2016-12-26 17:20:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long mis =sdf.parse(str).getTime();
		System.out.println(mis);
		long a=new Date().getTime();
		System.out.println(a);
		double i=(mis-a)/1000/3600/24;
		System.out.println(i);
	}

}
