import { Injectable ,inject} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PortfolioRequest,PortfolioResponse,PortfolioAnalyticsResponse } from '../models/portfolio.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PortfolioService {

 // /portfolios

 private baseUrl='http://localhost:8080/portfolios';
 private http=inject(HttpClient);

 // add portfolio
 addPortfolio(portreq:PortfolioRequest):Observable<PortfolioResponse>{
  return this.http.post<PortfolioResponse>(`${this.baseUrl}/add`,portreq)
 }
// show all
showAllPortfolios():Observable<PortfolioResponse[]>
{
  return this.http.get<PortfolioResponse[]>(`${this.baseUrl}/all`)
}
//show by username
getPortfolioByUsername(username: string): Observable<PortfolioResponse> {
    return this.http.get<PortfolioResponse>(`${this.baseUrl}/user/${username}`);
  }

  // show analytics
  showAnalytics(portfolioId:number):Observable<PortfolioAnalyticsResponse>
  {
    return this.http.get<PortfolioAnalyticsResponse>(`${this.baseUrl}/analytics/${portfolioId}`);
  }
  //update portfolio
  updatePortfolio(username: string, request: PortfolioRequest): Observable<PortfolioResponse> {
    return this.http.put<PortfolioResponse>(`${this.baseUrl}/edit/${username}`, request);
  }
  //delete portfolio
  deletePortfolio(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`, { responseType: 'text' });
  }

}
