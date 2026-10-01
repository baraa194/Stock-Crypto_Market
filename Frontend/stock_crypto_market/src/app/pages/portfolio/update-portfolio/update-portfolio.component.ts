import { Component, inject, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PortfolioItemRequest } from '../../../models/portfolio.model';
import { ActivatedRoute } from '@angular/router';
import { PortfolioService } from '../../../services/portfolio.service';

@Component({
  selector: 'app-update-portfolio',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './update-portfolio.component.html',
  styleUrl: './update-portfolio.component.scss'
})
export class UpdatePortfolioComponent implements OnInit {

  private route=inject(ActivatedRoute);
  private portfolioService=inject(PortfolioService)

  portfolioForm=new FormGroup({
username: new FormControl('', {
  nonNullable: true,
  validators: [
    Validators.required,
    Validators.minLength(4)
  ]
}),
    items:new FormArray<FormGroup<{
      quantity:FormControl<number>;
    average_buy_price:FormControl<number>;
    updated_at:FormControl<string>;
    assetname:FormControl<string>;
    }>
    >([])
  });

  get items():FormArray{
    return this.portfolioForm.get('items') as FormArray
  }
  ngOnInit(): void {
    const username=this.route.snapshot.paramMap.get('username')
     if (username){
    this.portfolioService.getPortfolioByUsername(username).subscribe(
   portfolio=>{
   this.portfolioForm.controls.username.setValue(portfolio.username)

    this.items.clear();

    portfolio.portfolioItems.forEach(item => {

      const group = this.createportfolioItems();

group.patchValue({
  ...item,

  assetname: item.assetName,

  updated_at: item.updated_at
    ? item.updated_at.slice(0, 16)
    : ''
});

      this.items.push(group);

    });


   }


  


    )
  }



  }

  createportfolioItems():FormGroup{
return new FormGroup({
  quantity:new FormControl(0,[
    Validators.required,
  ]),
  average_buy_price:new FormControl(0,[
    Validators.required,
  ]),
  updated_at:new FormControl('',[
    Validators.required,
  ]),
  assetname:new FormControl('',[
    Validators.required,
  ])


})
  }

  addPortfolioitems(){
    this.items.push(this.createportfolioItems());
  }

  editPortfolioitem(index:number)
  {
    const formItem=this.items.at(index) as FormGroup
   const updatedItem = formItem.getRawValue();

  console.log(updatedItem);
  }

    onSubmit(){
 
      if(this.portfolioForm.invalid){
        this.portfolioForm.markAllAsTouched();
        return;
      }
      const username =
    this.route.snapshot.paramMap.get('username');

  if (!username) return;

  const updatedPortfolio =
    this.portfolioForm.getRawValue();

  console.log(updatedPortfolio);

  this.portfolioService
    .updatePortfolio(username, updatedPortfolio)
    .subscribe({
      next: (response) => {
        console.log('Updated successfully', response);
      },

      error: (error) => {
        console.error('Update failed', error);
      }
    });

    }





}
