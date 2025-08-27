export interface AdsPage {
    content: Array<AdSummary>
    empty: boolean
    first: boolean
    last: boolean
    number: number
    numberOfElements: number
    pageable: Pageable
    size: number
    sort: Sort
    totalElements: number
    totalPages: number
}

export interface Pageable {
    pageNumber: number
    pageSize: number
    offset: number
    paged: boolean
    unpaged: boolean
    sorted: Sort
}

export interface Sort {
    empty: boolean
    sorted: boolean
    unsorted: boolean
}
