import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.scss'
})
export class AppComponent {

  menuItems = [
    {
      label: 'Documentos',
      icon: 'pi pi-file',
      routerLink: '/documents'
    },
    {
      label: 'Assistente de documentos',
      icon: 'pi pi-sparkles',
      routerLink: '/assistant'
    }
  ];

}
