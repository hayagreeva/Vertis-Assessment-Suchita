package com.aemvertis.core.models;

import com.aemvertis.core.pojos.MegaMenuPojo;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MegaMenuModel {
    @ValueMapValue
    private String megaMenuRootPath;
    @SlingObject
    private ResourceResolver resolver;
    private List<MegaMenuPojo> menuListPojo;
    private static final int LEVEL_LIMIT = 3;
    private static final String HIDE_ALL_SUBPAGE = "hideAllSubPageInNav";
    private static final String HIDE_IN_NAV = "hideInNav";
    private static final String TRUE = "true";
    private static final Logger LOG = LoggerFactory.getLogger(MegaMenuModel.class);

    @PostConstruct
    protected void init() {
        menuListPojo = new ArrayList<>();
        Resource resource = resolver.getResource(megaMenuRootPath);
        if (Objects.nonNull(resource)) {
            Page rootPage = resource.adaptTo(Page.class);
            menuListPojo = getMegaMenuPageList(rootPage, 1);
        }
    }

    List<MegaMenuPojo> getMegaMenuPageList(Page page, int level) {
        List<MegaMenuPojo> megaMenuSubList = new ArrayList<>();
        if (level <= LEVEL_LIMIT) {
            Iterator<Page> subMenuList = page.listChildren();
            while (subMenuList.hasNext()) {
                MegaMenuPojo megaMenuPojo = new MegaMenuPojo();
                Page childPage = subMenuList.next();
                ValueMap valueMap = childPage.getProperties();
                if (valueMap.containsKey(HIDE_IN_NAV) && valueMap.get(HIDE_IN_NAV).equals("true")) {
                    LOG.info("Page not included");
                } else {
                    megaMenuPojo.setMenuTitle(childPage.getTitle());
                    megaMenuPojo.setMenuLink(childPage.getPath());
                    if (valueMap.containsKey(HIDE_ALL_SUBPAGE) && valueMap.get(HIDE_ALL_SUBPAGE).equals(TRUE)) {
                        LOG.info("No subpage included");
                    } else {
                        List<MegaMenuPojo> megaMenuList = getMegaMenuPageList(childPage, level + 1);
                        megaMenuPojo.setChildPageList(megaMenuList);
                    }
                }
                megaMenuSubList.add(megaMenuPojo);
            }
            return megaMenuSubList;
        } else {
            return megaMenuSubList;
        }
    }

    public List<MegaMenuPojo> getMenuListPojo() {
        return menuListPojo;
    }
}