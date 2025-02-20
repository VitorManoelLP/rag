import { Component, EventEmitter, HostBinding, Input, Output } from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'sidebar-inside',
  standalone: true,
  templateUrl: './sidebar-inside.component.html',
  styleUrl: './sidebar-inside.component.scss',
  animations: [
    trigger('sidebarAnimation', [
      state('hidden', style({
        transform: 'translateX(100%)',
        opacity: 0
      })),
      state('visible', style({
        transform: 'translateX(0)',
        opacity: 1
      })),
      transition('hidden => visible', [
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')
      ]),
      transition('visible => hidden', [
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')
      ])
    ])
  ]
})
export class SidebarInsideComponent {

  @Input({ required: true }) title!: string;
  @Output() onClose: EventEmitter<void> = new EventEmitter<void>();

  sidebarOpen = false;

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  close() {
    this.toggleSidebar();
    this.onClose.emit();
  }

  @HostBinding('@sidebarAnimation')
  get animationState() {
    return this.sidebarOpen ? 'visible' : 'hidden';
  }

}
