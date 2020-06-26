package me.kazechin.janword.model;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @see com.github.pagehelper.PageInfo 的转换类
 * 减少不必要的字段
 *
 * @param <T>
 */
public class PageResult<T> {

	private final PageInfo<T> pageInfo;

	public PageResult(List<T> list) {
		this.pageInfo = new PageInfo<T>(list);
	}


	public int getPageNum() {
		return pageInfo.getPageNum();
	}

	public int getPageSize() {
		return pageInfo.getPageSize();
	}

	public long getTotal() {
		return pageInfo.getTotal();
	}

	public List<T> getDataset() {
		return pageInfo.getList();
	}

}
