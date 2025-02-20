import { PageParameter } from './page-parameter.model';

export default class HttpRequest {

  resource: string;
  pageParameter?: PageParameter;
  body?: any;
  filter?: string;

  constructor(
    resource: string,
    body?: any
  ) {
    this.resource = resource;
    this.body = body;
  }

  static ofResource(resource: string): HttpRequest {
    return new HttpRequest(resource);
  }

  static ofResourceAndBody(resource: string, body: any): HttpRequest {
    return new HttpRequest(resource, body);
  }

  withPageParameter(pageParameter: PageParameter): HttpRequest {
    this.pageParameter = pageParameter;
    return this;
  }

  withFilter(filter?: string): HttpRequest {
    this.filter = filter;
    return this;
  }

  public get pageQuery(): string {

    const queries: string[] = [];

    if (!this.pageParameter) {
      return '';
    }

    let param = `page=${this.pageParameter.page}&size=${this.pageParameter.size}`;

    if (this.pageParameter.sort) {
      queries.push(`${param}&sort=${this.pageParameter.sort.field},${this.pageParameter.sort?.order}`);
    } else {
      queries.push(param);
    }

    if (this.filter) {
      queries.push(`search=${this.filter}`);
    }

    return queries.join('&');
  }

  public get filterQuery(): string {
    return this.filter ? `search=${this.filter}` : '';
  }

}
