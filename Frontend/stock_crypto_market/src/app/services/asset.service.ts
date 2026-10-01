import { inject, Injectable } from '@angular/core';
import { AssetResponse,AssetUpdateDTO,AssetRequest } from '../models/asset.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AssetService {

private http=inject(HttpClient);
private baseUrl = 'http://localhost:8080/assets';

findAllAssets(): Observable<AssetResponse[]> {
    return this.http.get<AssetResponse[]>(`${this.baseUrl}/getall`);
  }
  findAssetById(id: number): Observable<AssetResponse> {
    return this.http.get<AssetResponse>(`${this.baseUrl}/${id}`);
  }

 
  addAsset(assetDto: AssetRequest): Observable<AssetResponse> {
    return this.http.post<AssetResponse>(`${this.baseUrl}/add`, assetDto);
  }

 
  updateAsset(id: number, assetDto: AssetUpdateDTO): Observable<AssetResponse> {
    return this.http.put<AssetResponse>(`${this.baseUrl}/edit/${id}`, assetDto);
  }

}
