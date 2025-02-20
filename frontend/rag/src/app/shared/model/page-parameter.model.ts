export class PageParameter {

  get pageSize() {
    return this.size;
  }

  constructor(
    readonly page: number,
    readonly size: number,
    readonly sort?: { field: string; order: 'asc' | 'desc' }
  ) {
    this.page = page;
    this.size = size;
    this.sort = sort;
  }

  static newInstance() {
    return new PageParameter(0, 10);
  }

  withSort(sort: { field: string; order: 'asc' | 'desc' }): PageParameter {
    return new PageParameter(this.page, this.size, sort);
  }

  next() {
    return new PageParameter(this.page + 1, this.size, this.sort);
  }

  previous() {
    return new PageParameter(this.page - 1, this.size, this.sort);
  }

  public static ofPage(page: number): PageParameter {
    return new PageParameter(page, 10);
  }

  public static ofPageSize(page: number, size: number): PageParameter {
    return new PageParameter(page, size);
  }

}
