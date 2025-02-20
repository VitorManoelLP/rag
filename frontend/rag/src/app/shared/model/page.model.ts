export interface Page<T> {
  content: T[];
  pageable: Pageable;
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export class PageUtil {
  static emptyPage<T>(): Page<T> {
    return {
      content: [],
      pageable: {
        offset: 0,
        pageNumber: 0,
        pageSize: 0,
        paged: false,
        unpaged: false,
      },
      totalPages: 0,
      totalElements: 0,
      last: false,
      size: 0,
      number: 0,
      first: false,
      numberOfElements: 0,
      empty: true,
    };
  }
}

export interface Pageable {
  offset: number;
  pageNumber: number;
  pageSize: number;
  paged: boolean;
  unpaged: boolean;
}
