import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { FileSizePipe } from '../../directive/file-size.pipe';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../../service/web-socket.service';

@Component({
  selector: 'app-pdf-upload-toolbar',
  standalone: true,
  imports: [CommonModule, ButtonModule, FileSizePipe],
  templateUrl: './pdf-upload-toolbar.component.html',
  styleUrl: './pdf-upload-toolbar.component.scss'
})
export class PdfUploadToolbarComponent implements OnDestroy, OnInit {

  @Output() fileUpload = new EventEmitter<File>();
  @Output() uploadFinished = new EventEmitter<void>();
  @Output() cancelUpload = new EventEmitter<void>();

  selectedFile: File | null = null;
  isDragging: boolean = false;
  isUploading: boolean = false;

  currentMessage: string = '';
  exitingMessage: string = '';
  nextMessage: string = '';
  showCurrentMessage: boolean = false;

  private messageIndex: number = 0;
  private messageSubscription?: Subscription;
  private animationTimeoutId?: number;

  private progressMessages: string[] = [
    'Iniciando o upload do documento...'
  ];

  constructor(private messageService: MessageService,
              private webSocketService: WebSocketService) {
  }

  async ngOnDestroy() {
    this.clearMessageInterval();
    await this.webSocketService.disconnect();
  }

  ngOnInit(): void {
    this.webSocketService.fromEvent('upload').subscribe((message: any) => {

      if (message.finished) {
        this.uploadFinished.emit();
        this.cancel();
      }

      if (message.error) {
        this.cancel();
        this.messageService.add({
          severity: 'error',
          summary: 'Erro desconhecido ao realizar upload',
          detail: message.message
        });
      }

      this.progressMessages.push(message.message);
      this.triggerMessageTransition();
    });
  }

  onFileSelected(event: any): void {
    const input = event['target'] as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (this.validateFile(file)) {
        this.selectedFile = file;
      }
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = true;
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;

    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      const file = event.dataTransfer.files[0];
      if (this.validateFile(file)) {
        this.selectedFile = file;
      }
    }
  }

  validateFile(file: File): boolean {
    if (file.type !== 'application/pdf') {
      this.messageService.add({
        severity: 'error',
        summary: 'Tipo inválido',
        detail: 'Por favor carregue um documento PDF'
      });

      return false;
    }

    return true;
  }

  removeFile(): void {
    this.selectedFile = null;
  }

  uploadFile(): void {
    if (!this.selectedFile) return;
    this.isUploading = true;
    this.startProgressMessages();
    this.fileUpload.emit(this.selectedFile);
  }

  cancel(): void {
    this.clearMessageInterval();
    this.selectedFile = null;
    this.isUploading = false;
    this.cancelUpload.emit();
  }

  private startProgressMessages(): void {
    this.messageIndex = 0;
    this.updateMessages();
    this.showCurrentMessage = true;
  }

  private triggerMessageTransition(): void {
    this.exitingMessage = this.currentMessage;
    this.showCurrentMessage = false;
    this.messageIndex++;
    if (this.messageIndex >= this.progressMessages.length) {
      this.messageIndex = 0;
    }
    this.updateMessages();
    this.animationTimeoutId = window.setTimeout(() => {
      this.exitingMessage = '';
      this.showCurrentMessage = true;
    }, 750);
  }

  private updateMessages(): void {
    this.currentMessage = this.progressMessages[this.messageIndex];
    this.nextMessage = this.progressMessages[(this.messageIndex + 1) % this.progressMessages.length];
  }

  private clearMessageInterval(): void {

    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
      this.messageSubscription = undefined;
    }

    if (this.animationTimeoutId) {
      window.clearTimeout(this.animationTimeoutId);
      this.animationTimeoutId = undefined;
    }

  }
}
