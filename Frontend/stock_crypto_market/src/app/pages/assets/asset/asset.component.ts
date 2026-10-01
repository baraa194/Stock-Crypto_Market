import { Component,OnInit,inject} from '@angular/core';
import { CommonModule,DecimalPipe } from '@angular/common';
import { FormsModule,NgForm } from '@angular/forms';
import { AssetService } from '../../../services/asset.service';
import { AssetRequest,AssetUpdateDTO,AssetResponse, AssetType } from '../../../models/asset.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-asset',
  standalone: true,
  imports: [RouterLink,DecimalPipe],
  templateUrl: './asset.component.html',
  styleUrl: './asset.component.scss'
})
export class AssetComponent implements OnInit {
private assetService = inject(AssetService);

  assetsList:AssetResponse[]=[];
  selectedAsset:AssetResponse|null=null;
  searchId: number | null = null;
  newAsset: AssetRequest = { symbol: '', name: '', currentPrice: 0, type: AssetType.CRYPTO };
  editMode: boolean = false;
  editingId: number | null = null;
  errorMessage: string = '';
  successMessage: string = '';

  ngOnInit(): void {
    this.loadAllAssets(); 
  }

loadAllAssets(): void {
    this.assetService.findAllAssets().subscribe({
      next: (data:any) => {
        this.assetsList = data;
        this.errorMessage = '';
      },
      error: (err:any) => this.errorMessage = 'Failed to load assets from server.'
    });
  }

}
