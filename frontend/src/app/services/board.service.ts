import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BoardService {
  private baseUrl = 'http://localhost:8080/api/boards';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getBoards(): Observable<any> {
    const headers = this.configureHeaders();
    return this.http.get<any[]>(this.baseUrl, { headers });
  }

  createBoard(name: string) : Observable<any>{
    const username = this.authService.getUsername(); 
    const headers = this.configureHeaders();
    return this.http.post<any>(this.baseUrl, { name, username }, { headers });
  }

  deleteBoard(boardId: number): Observable<any> {
    const headers = this.configureHeaders();
    return this.http.delete<any>(`${this.baseUrl}/${boardId}`, { headers });
  }

  createList(boardId: number, listName: string): Observable<any> {
    const headers = this.configureHeaders();
    return this.http.post<any>(`${this.baseUrl}/${boardId}/lists`, { name: listName }, { headers });
  }

  getListsForBoard(boardId: number): Observable<any>{
    const headers = this.configureHeaders();
    return this.http.get<any>(`${this.baseUrl}/${boardId}/lists`, { headers });
  }

  // Delete a list by its ID
  deleteList(boardId: number, listId: number): Observable<any> {
    const headers = this.configureHeaders();
    return this.http.delete<any>(`${this.baseUrl}/${boardId}/lists/${listId}`, { headers });
  }

  configureHeaders(): HttpHeaders{
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}
