export interface Card {
    id?: number; // Optional for new cards
    title: string;
    description: string;
    listId: number; // The list to which the card belongs
}
