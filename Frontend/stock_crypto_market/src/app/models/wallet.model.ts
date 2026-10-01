export interface WalletRequest {
  user_id: number;
  balance: number;
  currency: string;
}

export interface WalletResponse {
  id:number;
  user_id: number;
  balance: number;
  currency: string;
  updatedAt: string;
}
export interface LedgerEntryResponse {
  id: number;
  walletId: number;

  type: 'DEPOSIT' | 'WITHDRAWAL' | 'BUY' | 'SELL';

  amount: number;
  balanceBefore: number;
  balanceAfter: number;

  referenceType: 'TRADE' | 'WALLET';
  referenceId: number;

  description: string;
  createdAt: string;
}
export interface WalletTransactionRequest {
  walletId: number;
  amount: number;
}