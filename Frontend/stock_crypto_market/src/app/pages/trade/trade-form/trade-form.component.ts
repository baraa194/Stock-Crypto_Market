import { Component,inject, OnInit } from '@angular/core';
import {  FormControl, FormGroup,ReactiveFormsModule,Validators } from '@angular/forms';
import { BuyTradeRequest,SellTradeRequest,BuyTradeResponse,SellTradeResponse } from '../../../models/trade.model';
import { TradeService } from '../../../services/trade.service';


@Component({
  selector: 'app-trade-form',
  standalone: true,
  imports: [ReactiveFormsModule,],
  templateUrl: './trade-form.component.html',
  styleUrl: './trade-form.component.scss'
})
export class TradeFormComponent implements OnInit {

  private tradeService=inject(TradeService)
username:string='baraa';
portfolioId:number=2;
tradeType: 'buy' | 'sell' = 'buy';
buyTrades: BuyTradeResponse[] = [];
sellTrades: SellTradeResponse[] = [];
ngOnInit(): void {
   
    this.getBuyTrades();
  }

setTradeType(type: 'buy' | 'sell') {

  this.tradeType = type;

  if (type === 'buy') {
    this.getBuyTrades();
  } else {
    this.getSellTrades();
  }
}

tradeForm=new FormGroup({
assetName:new FormControl((''),Validators.required),
quantity:new FormControl<number|null>((null),[Validators.required,Validators.min(1)]),
  price_at_trade: new FormControl<number | null>(null)

});


getBuyTrades() {
  this.tradeService.getBuys(this.portfolioId).subscribe({
    next: (data) => {
      this.buyTrades = data;
    },
    error: (error) => {
      console.log(error);
    }
  });
}

getSellTrades() {
  this.tradeService.getSells(this.portfolioId).subscribe({
    next: (data) => {
      this.sellTrades = data;
    },
    error: (error) => {
      console.log(error);
    }
  });
}


 onSubmit() {


  if(this.tradeForm.invalid){
    this.tradeForm.markAllAsTouched();
    return;
  }

  if(this.tradeType==='buy')
  {
       const buyRequest: BuyTradeRequest = {
      assetName: this.tradeForm.value.assetName!,
      quantity: this.tradeForm.value.quantity!,
      username: this.username,
      portfolioId: this.portfolioId
    };

    this.tradeService.buyTrade(buyRequest).subscribe({
      next:(response)=>{
        console.log("buy successful",response)
          this.getBuyTrades();
      },error:(err)=>{
  console.log("buy error",err)
      }
    })

  }else{
    
    const sellRequest: SellTradeRequest = {
      assetName: this.tradeForm.value.assetName!,
      quantity: this.tradeForm.value.quantity!,
      price_at_trade: this.tradeForm.value.price_at_trade!,
      username: this.username,
      portfolioId: this.portfolioId
    };
       this.tradeService.sellTrade(sellRequest).subscribe({
      next: (response) => {
        console.log('Sell successful:', response);
          this.getSellTrades();
      },
      error: (error) => {
        console.log('Sell error:', error);
      }
    });

    
  }


  }






}
