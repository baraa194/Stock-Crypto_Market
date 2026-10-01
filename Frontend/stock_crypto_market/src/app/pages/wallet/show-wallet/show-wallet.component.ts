import { Component, OnInit, inject} from '@angular/core';
import { WalletRequest,WalletResponse } from '../../../models/wallet.model';
import { WalletService } from '../../../services/wallet.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-show-wallet',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './show-wallet.component.html',
  styleUrl: './show-wallet.component.scss'
})
export class ShowWalletComponent implements OnInit {

private walletService=inject(WalletService)
private router=inject(Router)
walletResponse:WalletResponse[]=[]

  ngOnInit(): void {
      this.showAll()
  }
deposit(id: number) {
  this.router.navigate(['/wallets', id, 'transaction', 'deposit']);
}

withdraw(id: number) {
  this.router.navigate(['/wallets', id, 'transaction', 'withdraw']);
}



  showAll(){

    this.walletService.getAllWallets().subscribe({
next: (responses) => this.walletResponse = responses,
    error:(err)=>console.log("error ",err)


    })

  }






}
