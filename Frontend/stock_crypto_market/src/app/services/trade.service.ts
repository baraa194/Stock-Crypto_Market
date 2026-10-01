import { Injectable , inject} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BuyTradeRequest,BuyTradeResponse,SellTradeRequest,SellTradeResponse } from '../models/trade.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TradeService {

  private http=inject(HttpClient);
  private baseUrl='http://localhost:8080/trades'


  buyTrade(trade: BuyTradeRequest): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/buy`,
      trade,
      { responseType: 'text' }
    );
  }

  sellTrade(trade: SellTradeRequest): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/sell`,
      trade,
      { responseType: 'text' }
    );
  }

  
  getBuys(portfolioId: number): Observable<BuyTradeResponse[]> {
    return this.http.get<BuyTradeResponse[]>(
      `${this.baseUrl}/getbuys/${portfolioId}`
    );
  }

 
  getSells(portfolioId: number): Observable<SellTradeResponse[]> {
    return this.http.get<SellTradeResponse[]>(
      `${this.baseUrl}/getsells/${portfolioId}`
    );
  }




 
}
