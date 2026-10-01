import { Component,inject, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { OrderResponse } from '../../models/order.models';

@Component({
  selector: 'app-view-order',
  standalone: true,
  imports: [],
  templateUrl: './view-order.component.html',
  styleUrl: './view-order.component.scss'
})
export class ViewOrderComponent implements OnInit {
    private orderService=inject(OrderService);
     portfolioId:number=2;
       portfolioOrders:OrderResponse[]=[];


ngOnInit(): void {
    this.viewAllOrders()
}


viewAllOrders(){

this.orderService.getPortfolioOrders(this.portfolioId).subscribe({

next:(response)=>{
this.portfolioOrders=response;
},
error:(err)=>{
console.log("error getting orders",err)
}


})
}
}