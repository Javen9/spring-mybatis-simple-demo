package mybatis.simple.demo.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalSize;
    private int currSize;
    private int currPage;
    private int maxPage;
    private List<T> items;

    public PageResponse(PageParams pageParams, int total, List<T> items) {
        this.items = items == null ? new ArrayList<>() : items;
        this.totalSize = Math.max(total, 0);
        this.currSize = this.items.size();
        this.currPage = pageParams.getPage();
        this.maxPage = (total - 1) / pageParams.getPageSize() + 1;

    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getCurrSize() {
        return currSize;
    }

    public int getCurrPage() {
        return currPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public List<T> getItems() {
        return items;
    }
}
