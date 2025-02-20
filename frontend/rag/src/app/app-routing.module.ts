import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'documents',
    loadComponent: () => import('./features/documents/documents.component').then(c => c.DocumentsComponent)
  },
  {
    path: '',
    redirectTo: 'documents',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'documents',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
