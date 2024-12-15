import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Card } from '../models/card.model';
import { AuthService } from './auth.service';
import { List } from '../models/list.model';

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private baseUrl = 'http://localhost:8080/api/cards';

  constructor(private http: HttpClient, private authService: AuthService) { }

  addCard(card: Card): Observable<Card> {
    const headers = this.configureHeaders();
    return this.http.post<Card>(`${this.baseUrl}/lists`, card, { headers });
  }

  getCards(listId: number): Observable<Card[]>{
    const headers = this.configureHeaders();
    return this.http.get<Card[]>(`${this.baseUrl}/lists/${listId}`, { headers: headers });
  }

  moveCard(cardId: number, newListId: number): Observable<void> {
    const headers = this.configureHeaders();
    return this.http.patch<void>(`${this.baseUrl}/${cardId}`, { newListId }, { headers });
  }

  configureHeaders(): HttpHeaders{
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}
