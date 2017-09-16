package mybatis.simple.demo.beans;

import java.io.Serializable;

public class PageParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int DEFAULT_PAGE = 1;
    private static int DEFAULT_PAGE_SIZE = 10;

    private int page = DEFAULT_PAGE;
    private int pageSize = DEFAULT_PAGE_SIZE;

    public PageParams() {
    }

    public PageParams(int page, int pageSize) {
        this.setPage(page);
        this.setPageSize(pageSize);
    }

    public int getPage() {
        return page;
    }

    public PageParams setPage(int page) {
        this.page = page < 1 ? DEFAULT_PAGE : page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageParams setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        return this;
    }

    public int getStart() {
        return (page - 1) * pageSize;
    }

    @Override
    public String toString() {
        return "{\"page\":" + page + ", \"pageSize\":" + pageSize + "}";
    }
}
