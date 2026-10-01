export interface PortfolioItemRequest{
    quantity:number,
    average_buy_price:number,
    updated_at:string,
    assetname:string
}
export interface PortfolioRequest
{
    username:string,
    items:PortfolioItemRequest[]
}
export interface PortfolioResponse{
    id:number,
    username:string,
    created_at:string,
    totalPNL:number,
    portfolioItems:PortfolioItemResponse[]
}
export interface PortfolioItemResponse
{
 assetName:string,
 quantity:number,
 average_buy_price:number,
 updated_at:string
        
 }
 export interface AssetAnalyticsResponse {
  assetName: string;
  quantity: number;
  averageBuyPrice: number;
  currentPrice: number;
  costBasis: number;
  marketValue: number;
  unrealizedPNL: number;
  returnPercentage: number;
  allocationPercentage: number;
}

export interface PortfolioAnalyticsResponse {
  portfolioId: number;
  userName: string;
  totalCostBasis: number;
  totalMarketValue: number;
  realizedPNL: number;
  unrealizedPNL: number;
  totalPNL: number;
  returnPercentage: number;
  assets: AssetAnalyticsResponse[];
}