package com.ccsw.tutorial.author.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

/**
 * @author ccsw
 *
 */
public class AuthorSearchDto {

    private PageableRequest pageable;       // gestiona las peticiones de paginacion

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}
