package com.bank.management.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 * This DTO provides pagination information along with the actual data,
 * following standard pagination patterns.
 *
 * @param <T> the type of data being returned
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class PageResponse<T> {

    /**
     * List of items for the current page.
     */
    private List<T> content;

    /**
     * Current page number (0-based).
     */
    private int pageNumber;

    /**
     * Page size.
     */
    private int pageSize;

    /**
     * Total number of elements across all pages.
     */
    private long totalElements;

    /**
     * Total number of pages.
     */
    private int totalPages;

    /**
     * Whether this is the first page.
     */
    private boolean first;

    /**
     * Whether this is the last page.
     */
    private boolean last;

    /**
     * Number of elements in the current page.
     */
    private int numberOfElements;

    /**
     * Whether the current page is empty.
     */
    private boolean empty;

    /**
     * Default constructor.
     */
    public PageResponse() {
    }

    /**
     * Constructor from Spring Data Page.
     *
     * @param page the Spring Data page
     */
    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.numberOfElements = page.getNumberOfElements();
        this.empty = page.isEmpty();
    }

    /**
     * Constructor with all fields.
     *
     * @param content the page content
     * @param pageNumber the page number
     * @param pageSize the page size
     * @param totalElements the total elements
     * @param totalPages the total pages
     * @param first whether first page
     * @param last whether last page
     * @param numberOfElements number of elements in page
     * @param empty whether page is empty
     */
    public PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements,
                       int totalPages, boolean first, boolean last, int numberOfElements, boolean empty) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
        this.numberOfElements = numberOfElements;
        this.empty = empty;
    }

    // Getters and Setters

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    /**
     * Checks if there are more pages after the current one.
     *
     * @return true if there are more pages
     */
    public boolean hasNext() {
        return !last;
    }

    /**
     * Checks if there are pages before the current one.
     *
     * @return true if there are previous pages
     */
    public boolean hasPrevious() {
        return !first;
    }

    /**
     * Gets the page number for the next page.
     *
     * @return the next page number, or -1 if no next page
     */
    public int getNextPageNumber() {
        return hasNext() ? pageNumber + 1 : -1;
    }

    /**
     * Gets the page number for the previous page.
     *
     * @return the previous page number, or -1 if no previous page
     */
    public int getPreviousPageNumber() {
        return hasPrevious() ? pageNumber - 1 : -1;
    }

    /**
     * Creates a PageResponse from a Spring Data Page.
     *
     * @param page the Spring Data page
     * @param <T> the data type
     * @return the page response
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page);
    }

    /**
     * Creates an empty PageResponse.
     *
     * @param <T> the data type
     * @return the empty page response
     */
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(java.util.Collections.emptyList(), 0, 10, 0, 0, true, true, 0, true);
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", numberOfElements=" + numberOfElements +
                '}';
    }
}