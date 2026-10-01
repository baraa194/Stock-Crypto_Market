import { Injectable,inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AssetPriceRequest,AssetPriceResponse } from '../models/asset-price.model';

@Injectable({
  providedIn: 'root'
})
export class AssetPriceService {

 private http=inject(HttpClient);
private baseUrl="http://localhost:8080/assetprices"

 addAssetPrice(assetpricereq: AssetPriceRequest): Observable<AssetPriceResponse> {
    return this.http.post<AssetPriceResponse>(`${this.baseUrl}/add`, assetpricereq);
  }

  findAllAssetPrices(assetName: string):Observable<AssetPriceResponse[]>{
    return this.http.get<AssetPriceResponse[]>(`${this.baseUrl}/${assetName}`);
  }



}
