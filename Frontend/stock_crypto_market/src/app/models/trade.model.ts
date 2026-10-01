export interface BuyTradeRequest{
quantity:number,
assetName:string,
username:string,
portfolioId:number

}
export interface SellTradeRequest{
  price_at_trade: number;
  quantity: number;
  assetName: string;
  username: string;
  portfolioId: number;
}
export interface BuyTradeResponse {
  quantity: number;
  assetName: string;
  username: string;
  portfolioId: number;
  PNL: number;
}
export interface SellTradeResponse {
  price_at_trade: number;
  quantity: number;
  assetName: string;
  username: string;
  portfolioId: number;
  realizedPNL: number;
  unrealizedPNL: number;
}