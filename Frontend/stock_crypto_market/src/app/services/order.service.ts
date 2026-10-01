import { Injectable,inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OrderRequest,OrderResponse } from '../models/order.models';
@Injectable({
  providedIn: 'root'
})
export class OrderService {
private http=inject(HttpClient);
private baseUrl = 'http://localhost:8080/orders';

  createOrder(request: OrderRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(
      this.baseUrl,
      request
    );
  }

 
  getPortfolioOrders(portfolioId: number): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(
      `${this.baseUrl}/portfolio/${portfolioId}`
    );
  }


  getPendingOrders(portfolioId: number): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(
      `${this.baseUrl}/portfolio/${portfolioId}/pending`
    );
  }


  cancelOrder(orderId: number): Observable<OrderResponse> {
    return this.http.patch<OrderResponse>(
      `${this.baseUrl}/${orderId}/cancel`,
      {}
    );
  }



}
