import { HttpClient } from '@angular/common/http';
import { Injectable,inject } from '@angular/core';
import { Observable } from 'rxjs';
import { WalletRequest, WalletResponse, LedgerEntryResponse ,WalletTransactionRequest} from '../models/wallet.model';

@Injectable({
  providedIn: 'root'
})
export class WalletService {


  private http=inject(HttpClient);
  private baseUrl='http://localhost:8080/wallets';

  // ADD WALLET
  addWallet(wallet: WalletRequest): Observable<string> {

    return this.http.post(
      `${this.baseUrl}/add`,
      wallet,
      { responseType: 'text' }
    );
  }

// deposite
deposit(request: WalletTransactionRequest): Observable<WalletResponse> {
  return this.http.post<WalletResponse>(
    `${this.baseUrl}/deposit`,
    request
  );
}
//withdrawl
withdraw(request: WalletTransactionRequest): Observable<WalletResponse> {
  return this.http.post<WalletResponse>(
    `${this.baseUrl}/withdraw`,
    request
  );
}
//view trnsactions(Ledger enteries)
viewTransactions(id:number):Observable<LedgerEntryResponse[]>
{
 return this.http.get<LedgerEntryResponse[]>(
      `${this.baseUrl}/${id}/ledger`
    );
}



  // UPDATE WALLET
  updateWallet(id: number, wallet: WalletRequest): Observable<WalletResponse> {

    return this.http.put<WalletResponse>(
      `${this.baseUrl}/edit/${id}`,
      wallet
    );
  }


  // GET WALLET BY ID
  getWalletById(id: number): Observable<WalletResponse> {

    return this.http.get<WalletResponse>(
      `${this.baseUrl}/${id}`
    );
  }


  // GET ALL WALLETS
  getAllWallets(): Observable<WalletResponse[]> {

    return this.http.get<WalletResponse[]>(
      `${this.baseUrl}/all`
    );
  }


  // DELETE WALLET
  deleteWallet(id: number): Observable<string> {

    return this.http.delete(
      `${this.baseUrl}/delete/${id}`,
      { responseType: 'text' }
    );
  }


 
}
