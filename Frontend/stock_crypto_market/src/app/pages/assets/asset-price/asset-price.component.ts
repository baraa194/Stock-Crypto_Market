import { Component, OnInit } from '@angular/core';
import { AssetPriceService } from '../../../services/asset-price.service';
import { inject } from '@angular/core';
import { AssetPriceResponse,AssetPriceRequest } from '../../../models/asset-price.model';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-asset-price',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './asset-price.component.html',
  styleUrl: './asset-price.component.scss'
})
export class AssetPriceComponent implements OnInit {

private assetpriceservice=inject(AssetPriceService);
private route=inject(ActivatedRoute)

assetPricesList:AssetPriceResponse[]=[];
assetName:string='';

// get assetprices
getAssetPricesByname():void{
if(!this.assetName.trim()) {return;}
this.assetpriceservice.findAllAssetPrices(this.assetName).subscribe({
next:(res)=>{
  this.assetPricesList=res
  console.log('Data received:', res);
},
error :(err)=> {
    console.error(err);
}

});


}

// add new price
addAssetPrice(assetpricereq:AssetPriceRequest ): void {   
    this.assetpriceservice.addAssetPrice(assetpricereq).subscribe({
      next: () => {
   
        this.getAssetPricesByname();
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

 ngOnInit(): void{

this.route.paramMap.subscribe(params => {
    const name = params.get('assetName');
    if (name) {
      this.assetName = name;
      this.getAssetPricesByname();
    }
  });

 }





}
