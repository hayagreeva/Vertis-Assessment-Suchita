package com.aemvertis.core.pojos;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import java.util.List;

@Model(adaptables = Resource.class)
public class MegaMenuPojo {

    private String menuTitle;

    private String menuLink;

    private List<MegaMenuPojo> childPageList;

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitles) {
        this.menuTitle = menuTitles;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLinks) {
        this.menuLink = menuLinks;
    }

    public List<MegaMenuPojo> getChildPageList() {
        return childPageList;
    }

    public void setChildPageList(List<MegaMenuPojo> childPageLists) {
        this.childPageList = childPageLists;
    }
}
