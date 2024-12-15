import { Card } from "./card.model";

export interface List {
    id: number;
    name: string;
    boardId: number; // The ID of the board the list belongs to
    cards: Card[]; // Cards that belong to the list
}
