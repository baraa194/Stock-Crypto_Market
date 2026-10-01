export interface AssetResponse{
    id:number,
name:string,
symbol:string,
type:AssetType,
currentPrice:number

}
export interface AssetRequest{

name:string,
symbol:string,
type:AssetType,
currentPrice:number

}
export interface AssetUpdateDTO{
   name:string,
  symbol:string,
  type:AssetType, 
}

export enum AssetType{
    CRYPTO='CRYPTO',
    STOCK='STOCK',
    ETF='EFT',
    FOREX='FOREX'
}
