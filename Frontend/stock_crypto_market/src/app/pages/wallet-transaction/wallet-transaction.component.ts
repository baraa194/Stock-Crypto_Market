import { Component,inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {  FormControl, FormGroup,ReactiveFormsModule,Validators } from '@angular/forms';
import { WalletService } from '../../services/wallet.service';
import { WalletTransactionRequest } from '../../models/wallet.model';

@Component({
  selector: 'app-wallet-transaction',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './wallet-transaction.component.html',
  styleUrl: './wallet-transaction.component.scss'
})
export class WalletTransactionComponent  implements OnInit{

walletId!: number;
transactionType!: 'deposit' | 'withdraw';
private route=inject(ActivatedRoute);
private walletService=inject(WalletService)

ngOnInit(): void {

  this.walletId = Number(
    this.route.snapshot.paramMap.get('walletId')
  );

  this.transactionType =
    this.route.snapshot.paramMap.get('type') as 'deposit' | 'withdraw';
}

transactionForm = new FormGroup({
  amount: new FormControl<number | null>(null, [
    Validators.required,
    Validators.min(0.01)
  ])
});


submit() {

  if (this.transactionForm.invalid) return;

  const request: WalletTransactionRequest = {
    walletId: this.walletId,
    amount: this.transactionForm.value.amount!
  };

  if (this.transactionType === 'deposit') {

    this.walletService.deposit(request).subscribe({
      next: response => {
        console.log('Deposit successful', response);
      },
      error: error => {
        console.error(error);
      }
    });

  } else {

    this.walletService.withdraw(request).subscribe({
      next: response => {
        console.log('Withdrawal successful', response);
      },
      error: error => {
        console.error(error);
      }
    });

  }
}
}
