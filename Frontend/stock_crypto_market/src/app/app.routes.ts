import { Routes } from '@angular/router';
import { AssetComponent } from './pages/assets/asset/asset.component';
import { AssetFormComponent } from './pages/assets/asset-form/asset-form.component';
import { AssetPriceComponent } from './pages/assets/asset-price/asset-price.component';
import { UpdatePortfolioComponent } from './pages/portfolio/update-portfolio/update-portfolio.component';
import { ShowallPortfolioComponent } from './pages/portfolio/showall-portfolio/showall-portfolio.component';
import { TradeFormComponent } from './pages/trade/trade-form/trade-form.component';
import { ShowWalletComponent } from './pages/wallet/show-wallet/show-wallet.component';
import { OrderComponent } from './pages/order/order.component';
import { ViewOrderComponent } from './pages/view-order/view-order.component';
import { TransactionsComponent } from './pages/transactions/transactions.component';
import { WalletTransactionComponent } from './pages/wallet-transaction/wallet-transaction.component';
import { PortfolioAnalyticsComponent } from './pages/portfolio-analytics/portfolio-analytics.component';

export const routes: Routes = [

{ path: 'assets', component: AssetComponent},
{path:'assets/add',component:AssetFormComponent},
{path:'assets/prices/:assetName',component:AssetPriceComponent},
{path:'portfolios',component:ShowallPortfolioComponent},
{path:'portfolios/edit/:username',component:UpdatePortfolioComponent},

  {
path:'trade',component:TradeFormComponent

  },
    {
path:'wallets',component:ShowWalletComponent

  },
  {
    path:'order',component:OrderComponent
  },
  {
    path:'viewOrders',component:ViewOrderComponent
  },

  {
    path:'wallets/:walletId/transactions',
    component:TransactionsComponent
  },
  {
  path: 'wallets/:walletId/transaction/:type',
  component: WalletTransactionComponent
},
{
path:'portfolios/analytics/:portfolioId',
component:PortfolioAnalyticsComponent


}
];
