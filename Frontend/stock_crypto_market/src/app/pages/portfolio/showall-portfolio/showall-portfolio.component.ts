import { Component, OnInit } from '@angular/core';
import { inject} from '@angular/core';
import { PortfolioRequest,PortfolioResponse } from '../../../models/portfolio.model';
import { PortfolioService } from '../../../services/portfolio.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, RouterOutlet } from '@angular/router';
@Component({
  selector: 'app-showall-portfolio',
  standalone: true,
  imports: [CommonModule, DatePipe, FormsModule, RouterLink,RouterOutlet],
  templateUrl: './showall-portfolio.component.html',
  styleUrl: './showall-portfolio.component.scss'
})
export class ShowallPortfolioComponent implements OnInit {
private portfolioService=inject(PortfolioService)
portfolios:PortfolioResponse[]=[]
usernameInput:string='';
isLoading:boolean=true;

showall():void{
  this.portfolioService.showAllPortfolios().subscribe({
  next:(responseList)=> this.portfolios=responseList,
  error:(err)=>console.error('error in data',err)

  })

}

onaddPortfolio():void{
  
const requsteddata:PortfolioRequest={
  username:this.usernameInput,
  items:[]
}
this.portfolioService.addPortfolio(requsteddata).subscribe({
  next:(res)=>{
    this.usernameInput='',
    this.showall()
  },
  error:(err)=>console.error("error adding portfolio : ",err)
});


}

ngOnInit(): void {
    this.showall();
}
}
