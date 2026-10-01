import { Component ,inject, OnInit} from '@angular/core';
import { PortfolioService } from '../../services/portfolio.service';
import { PortfolioAnalyticsResponse } from '../../models/portfolio.model';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-portfolio-analytics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './portfolio-analytics.component.html',
  styleUrl: './portfolio-analytics.component.scss'
})
export class PortfolioAnalyticsComponent implements OnInit{
  private portfolioService=inject(PortfolioService);
  private route=inject(ActivatedRoute)
analyticsResponse?: PortfolioAnalyticsResponse;
  isLoading: boolean = false;
  errorMessage: string = '';


  portfolioId?: number ;

  ngOnInit(): void {

  this.portfolioId=Number(this.route.snapshot.paramMap.get('portfolioId'))

    this.showAnalytics(this.portfolioId);
  }

  showAnalytics(id: number): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.portfolioService.showAnalytics(id).subscribe({
      next: (data) => {
        this.analyticsResponse = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching portfolio analytics:', err);
        this.errorMessage = 'Failed to load portfolio analytics. Please try again.';
        this.isLoading = false;
      }
    });
  }

colors = [
  '#6366f1', // indigo
  '#22d3ee', // cyan
  '#a78bfa', // violet
  '#34d399', // emerald
  '#f472b6', // pink
  '#fbbf24', // amber
  '#fb7185', // rose
  '#38bdf8'  // sky
];

getColor(index: number): string {
  return this.colors[index % this.colors.length];
}

getOffset(index: number): number {
  
  let offset = 25; 
  for (let i = 0; i < index; i++) {
    offset -= this.analyticsResponse!.assets[i].allocationPercentage;
  }
  return offset;
}

}
