import HttpRequest from '../../../shared/model/http-request.model';
import { PageParameter } from '../../../shared/model/page-parameter.model';
import { HttpClienteAdapter } from '../http-client.adapter';
import { HttpClient } from '@angular/common/http';

describe('HttpClientAdapter', () => {
  let httpClientAdapter: HttpClienteAdapter;
  let httpClientMock: HttpClient;

  beforeEach(() => {
    httpClientMock = jasmine.createSpyObj('HttpClient', [
      'get',
      'post',
      'patch',
      'delete',
    ]);
    httpClientAdapter = new HttpClienteAdapter(httpClientMock);
  });

  it('should be created', () => {
    expect(httpClientAdapter).toBeTruthy();
  });

  it('should call get', () => {
    httpClientAdapter.findAll(
      HttpRequest.ofResource('test').withPageParameter({ page: 1, size: 10 } as PageParameter)
    );
    expect(httpClientMock.get).toHaveBeenCalledWith('test?page=1&size=10');
  });

  it('should call post', () => {
    httpClientAdapter.post(
      HttpRequest.ofResourceAndBody('test', { test: 'test' })
    );
    expect(httpClientMock.post).toHaveBeenCalledWith('test', { test: 'test' });
  });

  it('should call patch', () => {
    httpClientAdapter.patch(
      HttpRequest.ofResourceAndBody('test', { test: 'test' })
    );
    expect(httpClientMock.patch).toHaveBeenCalledWith('test', { test: 'test' });
  });

  it('should call delete', () => {
    httpClientAdapter.delete(HttpRequest.ofResource('test'));
    expect(httpClientMock.delete).toHaveBeenCalledWith('test');
  });
});
