import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TextareaModule } from 'primeng/textarea';
import { FormsModule } from '@angular/forms';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';

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
  styleUrl: './assistant.component.scss'
})
export class AssistantComponent implements AfterViewChecked {

  messages: any[] = [];
  newMessage: string = '';
  isProcessing: boolean = false;

  @ViewChild('chatContainer') private chatContainer!: ElementRef;

  constructor() { }

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

    // Add user message
    this.addUserMessage(this.newMessage);
    const userQuery = this.newMessage;
    this.newMessage = '';

    // Show processing state
    this.isProcessing = true;

    // Simulate bot response after delay
    setTimeout(() => {
      // Create a more realistic response that looks like it came from document analysis
      const exampleResponse = `Based on my analysis of the provided documents, the answer to your query "${userQuery}" is:

The terms of service require a minimum notice period of 30 days for contract termination. According to section 8.2 of the agreement, all outstanding payments must be settled before the account can be closed.

You can initiate the cancellation process through your account dashboard or by contacting customer support at support@example.com.`;

      this.addBotMessage(exampleResponse);
      this.isProcessing = false;
    }, 1500);
  }

  addUserMessage(content: string): void {
    this.messages.push({
      content,
      sender: 'user',
      timestamp: new Date()
    });
  }

  addBotMessage(content: string): void {
    // Simulate parsing the response from backend
    // In a real app, this would come from your API
    const mockAnswer = {
      value: content,
      usedDocuments: ["document1.pdf", "document2.pdf"],
      confidence: 0.92
    };

    this.messages.push({
      content: mockAnswer.value,
      sender: 'bot',
      timestamp: new Date(),
      usedDocuments: mockAnswer.usedDocuments,
      confidence: mockAnswer.confidence
    });
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
