import { Component, EventEmitter, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { FileSizePipe } from '../../directive/file-size.pipe';

@Component({
  selector: 'app-pdf-upload-toolbar',
  standalone: true,
  imports: [CommonModule, ButtonModule, FileSizePipe],
  templateUrl: './pdf-upload-toolbar.component.html',
  styleUrl: './pdf-upload-toolbar.component.scss'
})
export class PdfUploadToolbarComponent {

  @Output() fileUploaded = new EventEmitter<File>();
  @Output() cancelUpload = new EventEmitter<void>();

  selectedFile: File | null = null;
  isDragging: boolean = false;
  isUploading: boolean = false;

  constructor(private messageService: MessageService) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
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

    setTimeout(() => {

      if (this.selectedFile) {
        this.isUploading = false;
        this.fileUploaded.emit(this.selectedFile);

        this.messageService.add({
          severity: 'success',
          summary: 'Upload Successful',
          detail: `${this.selectedFile?.name} has been uploaded`
        });

        this.selectedFile = null;
      }

    }, 2000);
  }

  cancel(): void {
    this.selectedFile = null;
    this.cancelUpload.emit();
  }

}
