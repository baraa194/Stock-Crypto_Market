import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AssetService } from '../../../services/asset.service'; // عدلي المسار حسب مشروعك
import { AssetType } from '../../../models/asset.model';   // عدلي المسار حسب مشروعك

@Component({
  selector: 'app-asset-form',
  standalone: true,
  imports: [FormsModule, RouterLink, CommonModule],
  templateUrl: './asset-form.component.html',
  styleUrl: './asset-form.component.scss'
})
export class AssetFormComponent implements OnInit {

  private assetService = inject(AssetService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  // Form state
  editMode = false;
  editingId: number | null = null;

  newAsset = {
    symbol: '',
    name: '',
    currentPrice: 0,
    type: AssetType.CRYPTO
  };

  // Messages
  successMessage = '';
  errorMessage = '';

  // Enum عشان نستخدمه في الـ HTML
  AssetType = AssetType;

  ngOnInit(): void {
    // نشوف لو فيه id في الرابط → يبقى Edit Mode
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.editMode = true;
      this.editingId = +id;
      this.loadAsset(this.editingId);
    }
  }

  // تحميل بيانات الـ Asset للتعديل
  loadAsset(id: number): void {
    this.assetService.findAssetById(id).subscribe({
      next: (asset) => {
        this.newAsset = {
          symbol: asset.symbol,
          name: asset.name,
          currentPrice: asset.currentPrice,
          type: asset.type
        };
      },
      error: () => {
        this.errorMessage = 'Failed to load asset data.';
      }
    });
  }

  // Submit (Add أو Edit)
  onSubmitAsset(form: NgForm): void {
    if (form.invalid) return;

    this.errorMessage = '';
    this.successMessage = '';

    if (this.editMode && this.editingId) {
      // ===== Update =====
      const updateData = {
        name: this.newAsset.name,
        symbol: this.newAsset.symbol,
        currentPrice: this.newAsset.currentPrice,
        type: this.newAsset.type
      };

      this.assetService.updateAsset(this.editingId, updateData).subscribe({
        next: () => {
          this.successMessage = 'Asset updated successfully!';
          setTimeout(() => {
            this.router.navigate(['/assets']);
          }, 1000);
        },
        error: () => {
          this.errorMessage = 'Failed to update asset.';
        }
      });

    } else {
      // ===== Create =====
      this.assetService.addAsset(this.newAsset).subscribe({
        next: () => {
          this.successMessage = 'Asset added successfully!';
          setTimeout(() => {
            this.router.navigate(['/assets']);
          }, 1000);
        },
        error: () => {
          this.errorMessage = 'Failed to add asset.';
        }
      });
    }
  }

  // Reset الفورم
  resetForm(form: NgForm): void {
    this.editMode = false;
    this.editingId = null;
    this.newAsset = {
      symbol: '',
      name: '',
      currentPrice: 0,
      type: AssetType.CRYPTO
    };
    form.resetForm({ type: AssetType.CRYPTO });
  }
}