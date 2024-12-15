import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common'
import { Card } from '../models/card.model';
import { CardService } from '../services/card.service';
import { BoardService } from '../services/board.service';
import { CdkDrag, DragDropModule } from '@angular/cdk/drag-drop';
import { FormsModule } from '@angular/forms'; // Import FormsModule

@Component({
  selector: 'app-list',
  imports: [FormsModule, DragDropModule, CommonModule,CdkDrag],
  templateUrl: './list.component.html',
  styleUrl: './list.component.css'
})
export class ListComponent {
  @Input() list: any = { cards: [] }; // Passed from parent dashboard component
  @Input() boardId!: number;

  @Output() listDeleted = new EventEmitter<number>()

  cards: Card[] = []; // Store the fetched cards here
  newCard: Partial<Card> = {};
  addingCardTo: number | null = null;
  
  constructor(private cardService: CardService, private boardService: BoardService) {}
  
  ngOnInit(): void {
    if (this.list?.id) {
      this.fetchCards(); // Fetch cards for the current list
    }
  }

  addCard(): void {
    this.addingCardTo = this.list.id;
    this.newCard = { title: '', description: '', listId: this.list.id };
  }

  cancelAddingCard(): void {
    this.addingCardTo = null;
  }

  submitCard(): void {
    if (this.newCard.title && this.newCard.description) {
      this.cardService.addCard(this.newCard as Card).subscribe({
        next: (card) => {
          if (!this.list.cards) {
            this.list.cards = [];
          }
          this.list.cards.push(card); // Add the new card to the list
          this.cancelAddingCard();
          this.refreshCards();
        },
        error: (err) => console.error('Error adding card:', err)
      });
    }
  }

  fetchCards(): void {
    this.cardService.getCards(this.list.id).subscribe((cards: Card[]) => {
      this.cards = cards;
    });
  }

  // Optionally, use fetchCards() to refresh the cards after an update
  refreshCards(): void {
    this.fetchCards();
  }

  // Delete a list
  deleteList(): void {
    if (this.list?.id && this.boardId) {
      this.boardService.deleteList(this.boardId, this.list.id).subscribe({
        next: () => {
          console.log(`List with ID ${this.list.id} deleted successfully.`);
          this.listDeleted.emit(this.list.id); // Notify parent about deletion
        },
        error: (err) => console.error('Error deleting list:', err)
      });
    } else {
      console.error('Board ID or List ID is undefined, cannot delete list.');
    }
  }
}
