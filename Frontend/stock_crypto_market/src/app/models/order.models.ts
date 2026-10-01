export interface OrderRequest {
  username: string;
  portfolioId: number;
  assetName: string;
  type: 'BUY' | 'SELL';
  quantity: number;
  targetPrice: number;
  orderType:'LIMIT' | 'STOP_LOSS' | 'TAKE_PROFIT';
    
   
}
export interface OrderResponse {
  id: number;
  username: string;
  portfolioId: number;
  assetName: string;
  type: 'BUY' | 'SELL';
  orderType:'LIMIT' | 'STOP_LOSS' | 'TAKE_PROFIT';
  quantity: number;
  targetPrice: number;
  status: 'PENDING' | 'PROCESSING'|'EXECUTED' | 'CANCELLED' | 'FAILED';
  createdAt: string;
  executedAt: string | null;
}