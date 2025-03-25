package xyz.yfrostyf.toxony.client.gui.journal;

import com.google.common.collect.ImmutableList;

public class JournalPages {
    private ImmutableList<PageScreen> pages;
    private int index;

    public JournalPages(){
        this.pages = ImmutableList.of();
        this.index = 0;
    }

    public JournalPages fill(ImmutableList<PageScreen> pages, String translateId){
        this.pages = pages;
        this.setPage(translateId);
        return this;
    }

    public PageScreen setPage(int index){
        PageScreen page = getPages().get(index);
        this.setIndex(index);
        return page;
    }

    public PageScreen setPage(String translateId){
        int index = getPage(translateId);
        PageScreen page = getPages().get(index);
        this.setIndex(index);
        return page;
    }

    public int getPage(String translateId){
        return getPages().indexOf(getPages().stream().filter(screen -> screen.translateID.matches(translateId)).findFirst()
                .orElseThrow((()-> new NullPointerException("Journal could not find page with " + translateId + " as the page doesn't exist"))));
    }

    public PageScreen getPageByString(String translateId){
        return getPages().stream().filter(screen -> screen.translateID.matches(translateId)).findFirst()
                .orElseThrow((()-> new NullPointerException("Journal could not find page with " + translateId + " as the page doesn't exist")));
    }

    public PageScreen updatePage(){
        PageScreen page = getPages().get(index);
        page.openScreen();
        return page;
    }

    public ImmutableList<PageScreen> getPages() {
        return pages;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean hasNext(){
        return index + 1 < pages.size();
    }
}
