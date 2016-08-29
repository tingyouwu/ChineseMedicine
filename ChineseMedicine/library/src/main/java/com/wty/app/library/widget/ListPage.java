package com.wty.app.library.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * @Decription 分页
 */
public class ListPage {

    private int pageIndex;//目前列表加载到第几分页
    private int pagePiece;//每页数据的数量

    private List<String> loadmoreIds = new ArrayList<String>();

    public ListPage(int pagePiece){
        this.pagePiece = pagePiece;
    }

    public int getPagePiece() {
        return pagePiece;
    }

    public void setPagePiece(int pagePiece) {
        this.pagePiece = pagePiece;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void pageNext(){
        this.pageIndex = pageIndex+1;
    }

    public void pageReset(){
        this.pageIndex = 0;
    }

    public void setLoadmoreIds(List<String> ids){
        loadmoreIds.clear();
        loadmoreIds.addAll(ids);
    }

    public String[] getIdsByPageIndex(){
        int index = getPageIndex();//已经加载到第几页
        int piece = getPagePiece();//每页大小

        int start = index * piece;
        int end = (index+1) * piece -1;

        if(start >= loadmoreIds.size()){
            return new String[]{};//没有下一页了
        }

        if(end > loadmoreIds.size()-1){

            end = loadmoreIds.size()-1;//最后一页了
        }
        List<String> ids = loadmoreIds.subList(start,end+1);
        return ids.toArray(new String[ids.size()]);
    }

}
