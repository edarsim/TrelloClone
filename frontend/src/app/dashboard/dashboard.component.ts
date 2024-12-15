import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { BoardService } from '../services/board.service';
import { CardService } from '../services/card.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ListComponent } from '../list/list.component';
import { Card } from '../models/card.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule, DragDropModule, ListComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  template: `<h1>Welcome to your Dashboard</h1>`,
})
export class DashboardComponent implements OnInit{
  boards: any[] = [];
  lists: any[] = [];
  selectedBoardId!: number;  // Store the selected board's ID
  newListName: string = '';  // Input for new list name

  constructor(private authService: AuthService, private router: Router, private boardService: BoardService, private cardService: CardService) {}
  ngOnInit(): void {
    this.fetchBoards();
  }

  getUsername(): string{
    return this.authService.getUsername()!;
  }

  logout(): void {
    this.authService.logout();
    console.log('Logged out successfully');
    this.router.navigate(['/login']); // Navigate to the login page
  }

  createBoard() {
    const boardName = prompt('Enter board name:')
    if(boardName){
      this.boardService.createBoard(boardName).subscribe({
        next: (newBoard) => {
          this.boards.push(newBoard);
          this.fetchBoards();
        },
        error: (err) => {
          console.error('Error creating board:', err);
        }
      })
    }
  }

  deleteBoard(id: number) {
    if(confirm('Are you sure you want to delete the board?')){
      this.boardService.deleteBoard(id).subscribe({
       next:  () => {
          this.boards = this.boards.filter((board) => board.id !== id);
          this.fetchBoards();
          if (this.boards.length > 0) {
            this.selectedBoardId = this.boards[0].id; // Select the first board
            this.fetchListsForBoard(this.selectedBoardId); // Fetch lists for the selected board
          } else {
            this.selectedBoardId = 0; // No board selected
            this.lists = []; // Clear the lists display
          }
        },
       error: (err) => {
        console.error('Error deleting board:', err);
       } 
      })
    }
  }

  onBoardSelect(event: Event): void {
    const target = event.target as HTMLSelectElement; // Cast to HTMLSelectElement
    const boardId = Number(target.value); // Safely access the value
    if (boardId) {
      this.selectedBoardId = boardId;
      console.log('Board selected with ID:', boardId);
      this.fetchListsForBoard(boardId); // Fetch the lists
    } else {
      console.error('Invalid board ID selected');
    }
  }

  fetchBoards(): void {
    this.boardService.getBoards().subscribe({
      next: (boards) => (this.boards = boards),
      error: (err) => console.error('Error fetching boards:', err)
    });
  }

  fetchListsForBoard(boardId: number): void {
    this.boardService.getListsForBoard(boardId).subscribe({
      next: (lists) => (this.lists = lists),
      error: (err) => console.error('Error fetching lists:', err)
    });
  }

  // Handle list creation
  createList(): void {
    if (this.selectedBoardId && this.newListName.trim()) {
      this.boardService.createList(this.selectedBoardId, this.newListName).subscribe(
        (newList) => {
          this.lists.push(newList); // Add the new list to the local array
          this.newListName = ''; // Clear the input field
          this.fetchListsForBoard(this.selectedBoardId);
        },
        (error) => {
          console.error('Error creating list:', error);
        }
      );
    }
  }

  // Delete a list
  deleteList(boardId: number, listId: number): void {
    this.boardService.deleteList(boardId, listId).subscribe(() => {
      // After successfully deleting, refresh the board data
      this.fetchBoards();
      this.fetchListsForBoard(boardId);
    });
  }

  deleteListFromDashboard(listId: number): void {
    // Remove the deleted list from the `lists` array
    this.lists = this.lists.filter((list) => list.id !== listId);
  }

  onCardDrop(event: any): void {
    console.log('Drag item data:', event.item.data); // Debugging
    console.log('Drop container data:', event.container.data); // Debugging
    console.log('Lists:', this.lists); // Debugging
    
    // Find the source list
    const currentContainerData = this.lists.find(
      list => list.cards && list.cards.some((card: { id: any; }) => card.id === event.item.data.id)
    );
  
    // Find the target list
    const targetContainerData = this.lists.find(
      list => list.cards === event.container.data
    );
  
    if (currentContainerData && targetContainerData) {
      console.log('Source list found:', currentContainerData);
      console.log('Target list found:', targetContainerData);
  
      // Remove card from the source list
      const cardIndex = currentContainerData.cards.findIndex((card: { id: any; }) => card.id === event.item.data.id);
      if (cardIndex > -1) {
        const [movedCard] = currentContainerData.cards.splice(cardIndex, 1);
  
        // Add card to the target list
        targetContainerData.cards.push(movedCard);
  
        // Optionally update the backend
        this.cardService.moveCard(movedCard.id, targetContainerData.id).subscribe({
          next: () => console.log('Card moved successfully'),
          error: err => console.error('Error moving card:', err),
        });
      }
    } else {
      console.error('Failed to find the source or target list');
    }
  }
}
