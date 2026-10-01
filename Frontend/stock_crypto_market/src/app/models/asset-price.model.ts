
export interface AssetPriceRequest{
price:number,
assetName:string
}

export interface AssetPriceResponse{
    price:number,
    recordedAt:string,
    assetName:string
    
}