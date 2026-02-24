package fr.uit.univparis8.tpair.tpair1.dto;

import java.util.List;

/**
 * DTO pour les réponses paginées
 */
public class PagedResponse<T> {
    
    public List<T> content;
    public int pageNumber;
    public int pageSize;
    public long totalElements;
    public int totalPages;
    public boolean hasNextPage;

    public PagedResponse(List<T> content, int pageNumber, int pageSize, 
                       long totalElements, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = pageNumber < (totalPages - 1);
    }
}
