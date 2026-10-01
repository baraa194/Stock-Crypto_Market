import { Component,inject, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { OrderRequest,OrderResponse } from '../../models/order.models';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent implements OnInit {

  private orderService=inject(OrderService);

  username:string='baraa';
  portfolioId:number=2;
  pendingOrders: OrderResponse[] = [];

  
  orderForm=new FormGroup({
    assetName:new FormControl('',Validators.required),
    
    quantity:new FormControl(null,[Validators.min(0.01),Validators.required]),
      targetPrice: new FormControl(null, [
    Validators.required,
    Validators.min(0.01)
  ]),
  type:new FormControl<'SELL'|'BUY'>('BUY',Validators.required),
   orderType: new FormControl<
    'LIMIT' | 'STOP_LOSS' | 'TAKE_PROFIT'
  >(
    'LIMIT',
    Validators.required
  )

  })
  ngOnInit() {
  this.getPendingOrders();
}

  onSubmit(){

  const orderreq:OrderRequest={
 username:this.username,
 portfolioId:this.portfolioId,
  assetName:this.orderForm.value.assetName!,
  quantity:this.orderForm.value.quantity!,
  targetPrice:this.orderForm.value.targetPrice!,
  type:this.orderForm.value.type!,
orderType: this.orderForm.value.orderType!
  }

this.orderService.createOrder(orderreq).subscribe({

  next: (response) => {
    console.log('Order created successfully', response);
    this.getPendingOrders();
  },

  error: (error) => {
    console.error('Error creating order', error);
  }

});


  }

getPendingOrders(){

  this.orderService.getPendingOrders(this.portfolioId).subscribe({
   next:(response)=>{

this.pendingOrders=response;
   },error(err) {
       console.log("error getting pending orders",err)
   },



  })

}

setTradeType(type:'SELL'|'BUY')
{
  this.orderForm.patchValue( {
    type:type   
    })

    if(type==='BUY')
    {
      this.orderForm.patchValue({
        orderType:'LIMIT'
      })
    }

}


cancelOrder(orderId:number){
  this.orderService.cancelOrder(orderId).subscribe({
  next:()=>{
    console.log("canceled successfully")
  },error(err) {
       console.log(" error canceled order",err)
  },


  })

}





  }





