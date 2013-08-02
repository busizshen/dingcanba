package com.dingcan.tag.dao;



import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.dingcan.tag.model.Tag;
import com.dingcan.tag.model.TagRowMapper;

public class TagDao extends JdbcDaoSupport{
	public int addTag(Tag tag){
		String sql = "INSERT INTO `t_tag` (`TagType`, `TagName`,`CreateDate`) VALUES "+
				"(?, ?);";
		Object[] param = new Object[] {tag.getTagType(),tag.getTagName(),tag.getCreateDate()};
		return getJdbcTemplate().update(sql, param);
	}
	
	/**
	 * ����һ���ؼ���,ͨ��ģ����ѯ,�����Ѿ����������Ĺؼ���,
	 * ������ڷ���list,���û�з���null
	 * @param key
	 * @return
	 * Green Lei
	 * 2012-12-5 ����5:58:13 2012
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> getTagByKey(String key){
		String sql = "select * from t_tag where TagName like '"+key+"%' ";
		return getJdbcTemplate().query(sql, new TagRowMapper());
	}
	
	/**
	 * �����ѯtag,��������
	 * @param count
	 * @return
	 * Green Lei
	 * 2012-12-5 ����6:17:45 2012
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> getTagByCount(int count){
		String sql = "select * from t_tag order by TagId desc limit 0,"+count;
		return getJdbcTemplate().query(sql, new TagRowMapper());
	}
}
