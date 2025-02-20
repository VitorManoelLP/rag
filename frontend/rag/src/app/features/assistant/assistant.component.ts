import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TextareaModule } from 'primeng/textarea';
import { FormsModule } from '@angular/forms';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';
import { DocumentService } from '../../shared/service/document.service';
import { HttpClientAdapter } from '../../shared/http/http-client.adapter';
import { ChatHistoryService } from '../../shared/service/chat-history.service';

@Component({
  selector: 'app-assistant',
  templateUrl: './assistant.component.html',
  imports: [
    CommonModule,
    TextareaModule,
    ButtonModule,
    FormsModule,
    ProgressSpinnerModule,
  ],
  providers: [
    DocumentService,
    HttpClientAdapter,
    ChatHistoryService
  ],
  styleUrl: './assistant.component.scss'
})
export class AssistantComponent implements AfterViewChecked, OnInit {

  messages: any[] = [];
  newMessage: string = '';
  isProcessing: boolean = false;
  typingMessage: any = null;
  typingIndex: number = 0;
  typingSpeed: number = 10;
  typingInterval: any = null;

  @ViewChild('chatContainer') private chatContainer!: ElementRef;

  constructor(private documentService: DocumentService, private chatHistoryService: ChatHistoryService) { }

  ngOnInit(): void {
    this.chatHistoryService.findAll()
      .subscribe((historical: any[]) => {

        historical.forEach(history => {
          this.addUserMessage(history.userMessage);

          const answer = history.botMessage;

          this.typingMessage = {
            content: answer.value,
            fullContent: answer.value,
            sender: 'bot',
            timestamp: new Date(),
            usedDocuments: answer.usedDocuments,
            confidence: answer.confidence,
            isTyping: true
          };

          this.messages.push(this.typingMessage);

        });

      });
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.chatContainer.nativeElement.scrollTop =
        this.chatContainer.nativeElement.scrollHeight;
    } catch(err) { }
  }

  sendMessage(): void {
    if (this.newMessage.trim() === '') return;

    this.addUserMessage(this.newMessage);
    const userQuery = this.newMessage;
    this.newMessage = '';

    this.isProcessing = true;

    this.documentService.answer(userQuery)
      .subscribe(answer => {
        this.isProcessing = false;
        this.startTypingEffect(answer);
      });
  }

  addUserMessage(content: string): void {
    this.messages.push({
      content,
      sender: 'user',
      timestamp: new Date()
    });
  }

  startTypingEffect(answer: any): void {

    this.typingMessage = {
      content: '',
      fullContent: answer.value,
      sender: 'bot',
      timestamp: new Date(),
      usedDocuments: answer.usedDocuments,
      confidence: answer.confidence,
      isTyping: true
    };

    this.messages.push(this.typingMessage);

    this.typingIndex = 0;
    if (this.typingInterval) {
      clearInterval(this.typingInterval);
    }
    this.typingInterval = setInterval(() => this.typeNextChunk(), 25);
  }

  typeNextChunk(): void {
    if (!this.typingMessage) return;
    const fullText = this.typingMessage.fullContent;
    const nextIndex = Math.min(this.typingIndex + this.typingSpeed, fullText.length);

    this.typingMessage.content = fullText.substring(0, nextIndex);
    this.typingIndex = nextIndex;

    if (nextIndex >= fullText.length) {
      clearInterval(this.typingInterval);
      this.typingMessage.isTyping = false;
      this.typingMessage = null;
    }
  }

  handleKeyUp(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  adjustTextareaHeight(textarea: HTMLTextAreaElement): void {
    textarea.style.height = 'auto';
    const newHeight = Math.min(Math.max(textarea.scrollHeight, 24), 200);
    textarea.style.height = `${newHeight}px`;
  }

}
