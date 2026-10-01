import { Component, OnInit ,inject} from '@angular/core';
import { WalletService } from '../../services/wallet.service';
import { ActivatedRoute } from '@angular/router';
import { LedgerEntryResponse } from '../../models/wallet.model';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.scss'
})
export class TransactionsComponent implements OnInit{

  walletId!:number;
  ledgerList:LedgerEntryResponse[]=[]

private walletService=inject(WalletService)
private route=inject(ActivatedRoute)
  ngOnInit(): void {

    this.walletId=Number(this.route.snapshot.paramMap.get('walletId'))
        this.showTransactions();
  }

   showTransactions()
  {
    this.walletService.viewTransactions(this.walletId).subscribe({

      next: (response) => {
     this.ledgerList=response
      },

      error: (error) => {
        console.error(error);
      }
    });

  }


}
