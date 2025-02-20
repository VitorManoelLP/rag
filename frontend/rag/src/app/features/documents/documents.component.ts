import { Component, OnInit } from '@angular/core';
import { PageParameter } from "../../shared/model/page-parameter.model";
import { DocumentService } from "../../shared/service/document.service";
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SidebarModule } from 'primeng/sidebar';
import { PaginatorModule } from 'primeng/paginator';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientAdapter } from '../../shared/http/http-client.adapter';
import { BrowserAnimationsModule, provideAnimations } from '@angular/platform-browser/animations';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { SidebarInsideComponent } from '../../shared/components/sidebar-inside/sidebar-inside.component';
import { Tooltip, TooltipModule } from 'primeng/tooltip';
import { FileSizePipe } from '../../shared/directive/file-size.pipe';
import { Page } from '../../shared/model/page.model';
import { ToolbarModule } from 'primeng/toolbar';
import { FileUploadModule } from 'primeng/fileupload';
import { MessageService } from 'primeng/api';
import { PdfUploadToolbarComponent } from '../../shared/components/pdf-upload-toolbar/pdf-upload-toolbar.component';

@Component({
  selector: 'app-documents',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    SidebarInsideComponent,
    InputTextModule,
    ButtonModule,
    SidebarModule,
    FileUploadModule,
    ToolbarModule,
    PaginatorModule,
    DatePipe,
    TooltipModule,
    FileSizePipe,
    PdfUploadToolbarComponent
  ],
  providers: [
    DocumentService,
    HttpClientAdapter
  ],
  templateUrl: './documents.component.html',
  styleUrl: './documents.component.scss'
})
export class DocumentsComponent implements OnInit {

  documents: any[] = [];
  selectedDocument: any | null = null;
  sidebarVisible: boolean = false;
  showUploadToolbar: boolean = false;

  page: PageParameter = PageParameter.newInstance();
  totalRecords: number = 0;

  constructor(private documentService: DocumentService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.loadDocuments();
  }

  loadDocuments() {
    this.documentService.findAll(this.page).subscribe((response: Page<any>) => {
      this.documents = response.content;
      this.totalRecords = response.totalElements;
    });
  }

  onPageChange({ first, rows }: any) {
    const page = (first / rows);
    this.page = PageParameter.ofPageSize(page, rows);
    this.loadDocuments();
  }

  showDetails(document: Document) {
    this.selectedDocument = document;
    this.sidebarVisible = !this.sidebarVisible;
  }

  handleFileUploaded(file: File) {

    const newDocument = {
      id: Date.now().toString(),
      name: file.name,
      size: file.size,
      createdAt: new Date()
    };

    this.documents = [newDocument, ...this.documents];
    this.totalRecords++;

    this.showUploadToolbar = false;

    this.messageService.add({
      severity: 'success',
      summary: 'Document Uploaded',
      detail: `${file.name} has been successfully added`
    });
  }

  cancelUpload() {
    this.showUploadToolbar = false;
  }

}
